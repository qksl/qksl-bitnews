package io.bitnews.common.core.service;


import io.bitnews.common.core.model.Email;
import org.springframework.stereotype.Service;

public interface IMailService {
	 /**
	  * 纯文本
	  * @param mail
	  * @throws Exception  void
	  */
	 void send(Email mail) throws Exception;
	 /**
	  * 富文本
	  * @param mail
	  * @throws Exception  void
	  *
	  */
	  void sendHtml(Email mail) throws Exception;

	 /**
	  * 队列
	  * @param mail
	  * @throws Exception  void
	  *
	  */
	 void sendQueue(Email mail) throws Exception;


}
