package br.edu.ifsp.scl.concurrenttranslator.model.livedata

import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.scl.concurrenttranslator.model.domain.Languages

object DeepTranslateLiveData {
    val languagesLiveData = MutableLiveData<Languages>()
}