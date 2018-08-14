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
import com.thing2x.smqd.SessionStore.SubscriptionData
import com.thing2x.smqd._
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

import scala.collection.JavaConverters._

// 2018. 8. 14. - Created by Kwon, Yeong Eon

/**
  *
  */
class RedisLayer(val redisHost: String, val redisPort: Int) {

  private val poolConfig = new JedisPoolConfig()
  private val pool = new JedisPool(poolConfig, redisHost, redisPort)

  private def redis: Jedis = pool.getResource

  def close(): Unit = {
    pool.close()
  }

  private def keySubs(clientId: ClientId)  = s"sessions/${clientId.id}/subscriptions"  // hash
  private def keyState(clientId: ClientId) = s"sessions/${clientId.id}/state"          // string ("true" or "false")

  def loadSubscriptions(clientId: ClientId): Seq[SubscriptionData] = {
    redis.hgetAll(keySubs(clientId)).asScala.map{ case (filterPath, qos) =>
      SubscriptionData(FilterPath(filterPath), QoS(qos.toInt))
    }.toSeq
  }

  def saveSubscription(clientId: ClientId, filterPath: FilterPath, qos: QoS): SmqResult = {
    redis.hset(keySubs(clientId), filterPath.toString, qos.id.toString).longValue() match {
      case 1 => // created
        SmqSuccess()
      case 0 => // already exists and updated
        SmqSuccess()
      case _ =>
        SmqSuccess()
    }
  }

  def deleteSubscription(clientId: ClientId, filterPath: FilterPath): SmqResult = {
    redis.hdel(keySubs(clientId), filterPath.toString).toLong match {
      case 1 => // deleted
        SmqSuccess()
      case 0 | _ => // no element and no operation permitted
        SmqSuccess()
    }
  }

  def setSessionState(clientId: ClientId, connected: Boolean): SmqResult = {
    redis.set(keyState(clientId), connected.toString)
    SmqSuccess()
  }

  def sessionState(clientId: ClientId): Boolean = {
    redis.get(keyState(clientId)) == "true"
  }
}
