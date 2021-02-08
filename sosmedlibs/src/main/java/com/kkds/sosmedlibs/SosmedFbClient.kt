package com.kkds.sosmedlibs

import android.util.Log
import com.google.gson.Gson
import com.sanardev.instagramapijava.InstaClient
import facebook4j.Facebook
import facebook4j.FacebookFactory
import facebook4j.auth.AccessToken
import facebook4j.conf.ConfigurationContext

class SosmedFbClient {

    lateinit var facebookClient: Facebook
    var gson = Gson()
    fun sosmedFbInstance(appId: String, appSecret: String,commaSeparetedPermissions: String, accessToken: String): Facebook{
        facebookClient = FacebookFactory().instance
        facebookClient.setOAuthAppId(appId, appSecret)
        facebookClient.setOAuthPermissions(commaSeparetedPermissions)
        facebookClient.oAuthAccessToken = AccessToken(accessToken, null)
        return facebookClient
    }

    fun getUserFbAccount(): FacebookUser{
        val urlAccount = "me?fields=id,name,picture"
        val accounts = facebookClient.callGetAPI(urlAccount).asJSONObject().toString()
        val dataAccount = gson.fromJson(accounts, FacebookUser::class.java)
        return dataAccount
    }

    fun getUserFbTimeline(): FacebookFeed{
        var url = "me/feed?fields=id,message,attachments,from,created_time,description"
        var response = facebookClient.callGetAPI(url)
        var data = response.asJSONObject().toString()
        var mdata = gson.fromJson(data, FacebookFeed::class.java)

        return mdata
    }

    fun getInstagramHastag(key: String, idIgAccount: String):InstagramTaggedMediaObj? {
        var urlReqIdHastag ="ig_hastag_search?user_id=$idIgAccount&q=$key"
        var reqIdHastag = facebookClient.callGetAPI(urlReqIdHastag).asJSONObject().toString()
        var dataHashtag = gson.fromJson(reqIdHastag, HashtagId::class.java)
        var idHashtag = dataHashtag.data.first().id
        var urlHastag = "$idHashtag/recent_media?user_id=17841431449073183&fields=id,caption,like_count,media_url,media_type,children"
        var tagedMedia = facebookClient.callGetAPI(urlHastag)
        Log.v("CRX", "TagMedia is : " + tagedMedia)
        Log.v("CRX", "TagMedia is Json Object =  " + tagedMedia.isJSONObject)
        var data = tagedMedia.asJSONObject().toString()
        var mdata = gson.fromJson(data, InstagramTaggedMediaObj::class.java)
        return mdata
    }

    fun getInstagramMention(idIgAccount: String): InstagramTaggedMediaObj?{
        var urlTag = "$idIgAccount/tags?fields=id,username,media_url,media_type,like_count,caption,timestamp"

        var tagedMedia = facebookClient.callGetAPI(urlTag)
        Log.v("CRX", "TagMedia is Json Array =  " + tagedMedia)
        Log.v("CRX", "TagMedia is Json Object =  " + tagedMedia.isJSONObject)
        var data = tagedMedia.asJSONObject().toString()
        var mdata = gson.fromJson(data, InstagramTaggedMediaObj::class.java)
        return mdata
    }


}