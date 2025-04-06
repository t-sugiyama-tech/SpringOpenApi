package com.projectname.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import com.projectname.common.exception.CustomSystemException;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3AccessOperator {
	
	@Value("${aws.s3.credentials.access-key}")
	private String s3AccessKey;
	
	@Value("${aws.s3.credentials.secret-key}")
	private String s3SecretKey;
	
	@Value("${aws.s3.prop.bucket-name}")
	private String s3BucketName;
	
	@Value("${aws.s3.prop.region}")
	private String s3Region;
	
	private S3Client s3Client;
	
	@PostConstruct
	public void init() {
		s3Client = S3Client.builder()
				.credentialsProvider(() -> (AwsBasicCredentials.create(s3AccessKey, s3SecretKey)))
				.region(Region.of(s3Region))
				.build();
	}
	
	public String fileUpload() throws CustomSystemException, Exception {
		
	    try {
			// mimeType取得
	    	String mimeType = "";
	    	// S3上のファイルパスを作成(バゲット名は不要)
	    	String s3Path = new StringBuffer()
	    		            .append("featureFunction") // 機能名
	    		            .append("/")
	    		            .append("fileName") // ファイル名
	    		            .toString();
	    	
	    	// Base64のバイト配列化
	       byte[] decode = Base64.getDecoder().decode("ファイルをBase64化された文字列");
	       long byteLength = (long) decode.length;
	       
	       // PutObjectRequestオブジェクト作成
	       PutObjectRequest putObjectRequset = PutObjectRequest.builder()
	    		   .bucket(s3BucketName)
	    		   .key(s3Path)
	    		   .contentType(mimeType) // ファイル拡張子　※文字コードを付与してcontentTypeにする必要かは確認
	    		   .contentLength(byteLength)
	    		   .checksumAlgorithm(ChecksumAlgorithm.SHA256)
	    		   .build();
	       
	       try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decode)) {
	    	   
	    	   s3Client.putObject(putObjectRequset, RequestBody.fromInputStream(inputStream, byteLength));
	    	   
	       } catch (SdkClientException | IOException ex) {
	    	   // ログ出力
	    	   // S3処理エラーを別エラーとして再スローする必要性の検討
	    	   throw new CustomSystemException("S3アップロード処理中エラーが発生しました。", ex);
	       }
	       
	       // S3登録結果をDBにも登録し、履歴を管理できるようにする
	       // Do Something
	       
	       // 処理に成功した場合の返却
	       return "";
	    	
		} catch (Exception ex) {
		       // ログ出力   
		       ex.printStackTrace();
		       return "";
		}
	}
	
	public String fileDownload() throws CustomSystemException, Exception {
		
		String base64Str = "";
		try {
			// DBから登録時のS3パスを取得
			String s3Path = "";
			// S3バゲットとファイルパスを指定
			GetObjectRequest objectRequest = GetObjectRequest.builder()
					.bucket(s3BucketName)
					.key(s3Path)
					.build();
			
		    try (ResponseInputStream<GetObjectResponse> ResInputStream = s3Client.getObject(objectRequest)) {
		    	// バイト配列データを取得
		    	byte[] encode = Base64.getEncoder().encode(IOUtils.toByteArray(ResInputStream));
		    	base64Str = new String(encode);
		    	
		    } catch (SdkClientException ex) {
		    	   // S3処理エラーを別エラーとして再スローする必要性の検討
		    	   throw new CustomSystemException("S3ダウンロード処理中エラーが発生しました。", ex);
		    }
		    
		    // ファイルをdataURI書式に変換
		    String mimeType = "";
		    base64Str  = new StringBuffer()
		            .append("data")
		            .append(mimeType)
		            .append(";base64,")
		            .append(base64Str)
		            .toString();
		    
		    // 処理に成功した場合の返却(base64Strを呼び出し側に返す)
		    return "";
		} catch (Exception ex) {
			// ログ出力
			ex.printStackTrace();
			return "";
		}
	}
	
	public void fileDelete() throws Exception {
		
		try {
			
			// 削除対象ファイル情報を取得
			// バケット名とS３のファイルパスを指定
			ListObjectsV2Request listObjectV2Request = ListObjectsV2Request.builder()
					.bucket(s3BucketName)
					.prefix("s3Path")
					.delimiter("/")
					.build();
			
			ListObjectsV2Response objectsV2ReslList = s3Client.listObjectsV2(listObjectV2Request);
								
			// 削除するオブジェクトのリストを作成
			ArrayList<ObjectIdentifier> keys = new ArrayList<>();
			for (S3Object s3Object : objectsV2ReslList.contents()) {
				ObjectIdentifier s3ObjectId = ObjectIdentifier.builder()
						.key(s3Object.key())
						.build();
				keys.add(s3ObjectId);
			}
			
			// オブジェクトが存在する場合、削除処理を実行
			if (!keys.isEmpty()) {
				Delete deleteTarget = Delete.builder()
						.objects(keys)
						.build();
				
				DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
						.bucket(s3BucketName)
						.delete(deleteTarget)
						.build();
				
				// S3ファイル削除
				s3Client.deleteObjects(deleteObjectsRequest);
			}
			
		} catch (Exception ex) {
			// ログ出力
			ex.printStackTrace();
		}
	}

}
