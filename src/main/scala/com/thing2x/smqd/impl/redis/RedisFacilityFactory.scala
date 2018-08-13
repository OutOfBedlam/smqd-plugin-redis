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

import com.thing2x.smqd.SessionStoreDelegate
import com.thing2x.smqd.impl.DefaultFacilityFactory
import com.typesafe.config.Config
import redis.clients.jedis.{JedisPool, JedisPoolConfig}

import scala.concurrent.ExecutionContext

// 2018. 8. 13. - Created by Kwon, Yeong Eon

/**
  *
  */
class RedisFacilityFactory(config: Config, ec: ExecutionContext) extends DefaultFacilityFactory(config, ec) {

  private val redisHost = config.getString("smqd.redis.host")
  private val redisPort = config.getInt("smqd.redis.port")

  private val poolConfig = new JedisPoolConfig()
  private val pool = new JedisPool(poolConfig, redisHost, redisPort)

  override def sessionStoreDelegate: SessionStoreDelegate = {
    implicit val executionContext: ExecutionContext = ec
    new RedisSessionStoreDelegate(pool)
  }

  override def release(): Unit = {
    pool.close()
  }
}
