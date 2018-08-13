package com.thing2x.smqd.impl.redis

import com.thing2x.smqd.QoS.QoS
import com.thing2x.smqd._

import scala.concurrent.Future

// 2018. 8. 13. - Created by Kwon, Yeong Eon

/**
  *
  */
class RedisSessionStoreDelegate extends SessionStoreDelegate  {
  override def createSession(clientId: ClientId, cleanSession: Boolean): Future[SessionStore.InitialData] = ???

  override def flushSession(token: SessionStore.SessionStoreToken): Future[SmqResult] = ???

  override def saveSubscription(token: SessionStore.SessionStoreToken, filterPath: FilterPath, qos: QoS): Future[SmqResult] = ???

  override def deleteSubscription(token: SessionStore.SessionStoreToken, filterPath: FilterPath): Future[SmqResult] = ???

  override def loadSubscriptions(token: SessionStore.SessionStoreToken): Seq[SessionStore.SubscriptionData] = ???

  override def storeBeforeDelivery(token: SessionStore.SessionStoreToken, topicPath: TopicPath, qos: QoS, isReatin: Boolean, msgId: Int, msg: Any): Future[SmqResult] = ???

  override def deleteAfterDeliveryAck(token: SessionStore.SessionStoreToken, msgId: Int): Future[SmqResult] = ???

  override def updateAfterDeliveryAck(token: SessionStore.SessionStoreToken, msgId: Int): Future[SmqResult] = ???

  override def deleteAfterDeliveryComplete(token: SessionStore.SessionStoreToken, msgId: Int): Future[SmqResult] = ???

  override def setSessionState(clientId: ClientId, connected: Boolean): Future[SmqResult] = ???

  override def snapshot(search: Option[String]): Future[Seq[SessionStore.ClientData]] = ???
}
