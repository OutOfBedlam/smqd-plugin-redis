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

import java.text.ParseException

import com.thing2x.smqd.{FilterPath, QoS}
import com.thing2x.smqd.QoS.QoS
import com.thing2x.smqd.SessionStore.SubscriptionData
import spray.json.{JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

// 2018. 8. 14. - Created by Kwon, Yeong Eon

/**
  *
  */
object RedisSessionStoreSerializer {

  implicit object FilterPathFormat extends RootJsonFormat[FilterPath] {
    override def write(obj: FilterPath): JsValue = JsString(obj.toString)
    override def read(json: JsValue): FilterPath = json match {
      case js: JsString => FilterPath(js.value)
      case _ => throw new ParseException("redis java serialization failed (FilterPath)", 0)
    }
  }

  implicit object QoSFormat extends RootJsonFormat[QoS] {
    override def write(obj: QoS): JsValue = JsNumber(obj.id)
    override def read(json: JsValue): QoS = json match {
      case js: JsNumber => QoS(js.value.intValue())
      case _ => throw new ParseException("redis java serialization failed (QoS)", 0)
    }
  }

  implicit object SubscriptionDataFormat extends RootJsonFormat[SubscriptionData] {
    override def write(obj: SubscriptionData): JsValue = {
      JsObject(
        "filterPath" -> FilterPathFormat.write(obj.filterPath),
        "qos" -> QoSFormat.write(obj.qos))
    }
    override def read(json: JsValue): SubscriptionData = {
      SubscriptionData(
        FilterPathFormat.read(json.asJsObject.fields("filterPath")),
        QoSFormat.read(json.asJsObject.fields("qos")))
    }
  }
}
