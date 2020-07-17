package com.example.juniorproject.db.realm

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * https://developer88.tistory.com/208 참고
 * LiveDataWrapper 클래스
 * LiveData 와 RealmObject 를 같이 사용하기 위한 Wrapper 클래스
 * Realm 의 RealmResults<T> 타입의 결과 값이 나오면, 결과값으로 value 에 set 해주는 listener
 * 라이프 사이클에 맞춰 listener 를 등록 및 해제
 * LiveRealmData<T:RealmModel> 은 Kotlin Generic 의 UpperBound 로 https://developer88.tistory.com/212 3-1 Upper Bound 참조
 */
class RealmLiveData<T:RealmModel>(val realmResults: RealmResults<T>) : LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>>{
        value = it
    }

    override fun onActive() = realmResults.addChangeListener(listener)

    override fun onInactive() = realmResults.removeChangeListener(listener)

}