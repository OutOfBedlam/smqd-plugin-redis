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

package com.thing2x.smqd.impl.redis.test

import com.thing2x.smqd.{ClientId, FilterPath, QoS}
import com.thing2x.smqd.impl.redis.RedisLayer
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

// 2018. 8. 14. - Created by Kwon, Yeong Eon

/**
  *
  */
class LayerTest extends WordSpec with BeforeAndAfterAll with Matchers with StrictLogging {

  var layer: RedisLayer = _

  override def beforeAll(): Unit = {
    layer = new RedisLayer("127.0.0.1", 6379)
  }

  override def afterAll(): Unit = {
    layer.close()
  }

  "redis layer" must {

    val clientId = ClientId("sub_1", "test_1")
    val ntime = 10
    logger.trace(s"clientId = $clientId")

    "subscribes" in {
      0 until ntime foreach { n =>
        layer.saveSubscription(clientId, FilterPath(s"sensor/1$n/temp"), QoS.AtMostOnce)
      }

      val subs = layer.loadSubscriptions(clientId)
      assert(subs.length == ntime)

      val strs = subs.map(_.filterPath.toString)
      0 until ntime foreach { n =>
        assert(strs.contains(s"sensor/1$n/temp"))
      }
    }

    "unsubscribes" in {
      0 until ntime foreach { n =>
        layer.deleteSubscription(clientId, FilterPath(s"sensor/1$n/temp"))
      }

      val subs = layer.loadSubscriptions(clientId)
      assert(subs.isEmpty)
    }

    "connected" in {
      layer.setSessionState(clientId, connected = true)
      assert(layer.sessionState(clientId))
      layer.setSessionState(clientId, connected = false)
      assert(!layer.sessionState(clientId))
    }
  }
}
