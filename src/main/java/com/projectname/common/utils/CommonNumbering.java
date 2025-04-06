package com.projectname.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 機能間で整合の取れたシーケンスを採番するための機能
@Component
public class CommonNumbering {

	/** どこまでの例外を補足する必要があるか、トランザクションを貼りなおす必要性も検討.  */
	// synchronizedで排他制御を実現
	@Transactional(noRollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	 public synchronized Integer getCommonSeqOfFun() {
		 
		// 機能間で整合の取れたシーケンス
		Integer seqNum = null;
		
		// 条件に合致した採番値を採番マスタから取得し、採番値が最大値を超過していないか等も検討
		// SELECT　TABLE
		/** synchronizedが有効な場合はJVM上に限られる為、アプリが複数ある場合、楽観または悲観ロックを行って排他制御が実現できるようにする.  */ 
		// 採番マスタに適切な値を設定し、シーケンスを更新
		// UPDATE　TABLE
		 
		 return seqNum;
	 }
	
}
