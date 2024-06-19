package br.edu.ifsp.scl.concurrenttranslator.model.livedata

import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.scl.concurrenttranslator.model.domain.Languages
import br.edu.ifsp.scl.concurrenttranslator.model.domain.Translation

object DeepTranslateLiveData {
    val languagesLiveData = MutableLiveData<Languages>()
    val translationLiveData = MutableLiveData<Translation>()
    val errorLiveData = MutableLiveData<String>()
}