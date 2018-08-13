package com.thing2x.smqd.impl.redis

import com.thing2x.smqd.SessionStoreDelegate
import com.thing2x.smqd.impl.DefaultFacilityFactory
import com.typesafe.config.Config

// 2018. 8. 13. - Created by Kwon, Yeong Eon

/**
  *
  */
class RedisFacilityFactory(config: Config) extends DefaultFacilityFactory(config) {

  override def sessionStoreDelegate: SessionStoreDelegate = {

    new RedisSessionStoreDelegate()
  }
}
