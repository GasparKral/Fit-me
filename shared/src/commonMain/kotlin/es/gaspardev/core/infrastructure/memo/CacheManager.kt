package es.gaspardev.core.infrastructure.memo

import java.io.*

object CacheManager {

    private var cache: HashMap<CacheRef, Any> = hashMapOf()

    fun loadCache() {
        ContentManager.initialize()
        if (ContentManager.existCache()) {
            try {
                ObjectInputStream(FileInputStream(File(ContentManager.cacheFolder, "cache.bin"))).use { ois ->
                    cache = ois.readObject() as HashMap<CacheRef, Any>
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveCache() {
        try {
            ObjectOutputStream(FileOutputStream(File(ContentManager.cacheFolder, "cache.bin"))).use { oos ->
                oos.writeObject(cache)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveValue(key: CacheRef, value: Any) {
        cache[key] = value
    }

    fun hasKey(key: CacheRef): Boolean {
        return cache.containsKey(key)
    }

    fun getValue(key: CacheRef): Any {
        return cache[key]!!
    }
}

sealed class CacheRef : Serializable {
    object RememberUserName : CacheRef() {
        private fun readResolve(): Any = RememberUserName
    }
}
