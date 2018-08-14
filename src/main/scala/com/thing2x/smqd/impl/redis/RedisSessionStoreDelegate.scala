// Copyright 2018 UANGEL
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.thing2x.smqd.impl.redis

import com.thing2x.smqd.QoS.QoS
import com.thing2x.smqd.SessionStore.{InitialData, SessionStoreToken, SubscriptionData}
import com.thing2x.smqd._
import com.thing2x.smqd.impl.redis.RedisSessionStoreDelegate.Token
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContext, Future}

// 2018. 8. 13. - Created by Kwon, Yeong Eon

object RedisSessionStoreDelegate {
  case class Token(clientId: ClientId, cleanSession: Boolean) extends SessionStoreToken
}

class RedisSessionStoreDelegate(layer: RedisLayer)(implicit ec: ExecutionContext) extends SessionStoreDelegate with StrictLogging {

  override def createSession(clientId: ClientId, cleanSession: Boolean): Future[SessionStore.InitialData] = Future {
    logger.trace(s"[$clientId] *** createSession")
    val token = Token(clientId, cleanSession)

    val subscriptions = if (cleanSession) {
      // always create new session if cleanSession = true
      Nil
    }
    else {
      // try to restore previous session if cleanSession = false
      layer.loadSubscriptions(token.clientId)
    }
    InitialData(token, subscriptions)
  }

  override def flushSession(token: SessionStore.SessionStoreToken): Future[SmqResult] = Future {
    logger.trace(s"[${token.clientId}] *** flushSession")

    if (token.cleanSession) {
      SmqSuccess()
    }
    else {
      SmqSuccess()
    }
  }

  override def loadSubscriptions(token: SessionStore.SessionStoreToken): Future[Seq[SessionStore.SubscriptionData]] = Future {
    logger.trace(s"[${token.clientId}] *** loadSubscriptions")
    if (token.cleanSession) {
      Nil
    }
    else {
      layer.loadSubscriptions(token.clientId)
    }
  }

  override def saveSubscription(token: SessionStore.SessionStoreToken, filterPath: FilterPath, qos: QoS): Future[SmqResult] = Future {
    logger.trace(s"[${token.clientId}] *** saveSubscription ($filterPath -> ${qos.id})")
    layer.saveSubscription(token.clientId, filterPath, qos)
  }

  override def deleteSubscription(token: SessionStore.SessionStoreToken, filterPath: FilterPath): Future[SmqResult] = Future {
    logger.trace(s"[${token.clientId}] *** deleteSubscription ($filterPath)")
    layer.deleteSubscription(token.clientId, filterPath)
  }

  override def storeBeforeDelivery(token: SessionStore.SessionStoreToken, topicPath: TopicPath, qos: QoS, isReatin: Boolean, msgId: Int, msg: Any): Future[SmqResult] = Future {
    logger.trace(s"[${token.clientId}] *** storeBeforeDelivery")
    SmqSuccess()
  }

  override def deleteAfterDeliveryAck(token: SessionStore.SessionStoreToken, msgId: Int): Future[SmqResult] = Future {
    logger.trace(s"[${token.clientId}] *** deleteAfterDeliveryAck")
    SmqSuccess()
  }

  override def updateAfterDeliveryAck(token: SessionStore.SessionStoreToken, msgId: Int): Future[SmqResult] = Future {
    logger.trace(s"[${token.clientId}] *** updateAfterDeliveryAck")
    SmqSuccess()
  }

  override def deleteAfterDeliveryComplete(token: SessionStore.SessionStoreToken, msgId: Int): Future[SmqResult] = Future {
    logger.trace(s"[${token.clientId}] *** deleteAfterDeliveryComplete")
    SmqSuccess()
  }

  override def setSessionState(clientId: ClientId, connected: Boolean): Future[SmqResult] = Future {
    logger.trace(s"[$clientId] *** setSessionState (connected: $connected)")
    layer.setSessionState(clientId, connected)
  }

  override def snapshot(search: Option[String]): Future[Seq[SessionStore.ClientData]] = Future {
    logger.trace(s"*** snapshot")
    Nil
  }
}
