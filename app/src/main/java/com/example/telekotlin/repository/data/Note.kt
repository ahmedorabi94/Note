package com.example.telekotlin.repository.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tele")
data class Note(
    val title: String,
    val body: String,
    val signature: ByteArray?,
    val date: String

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (title != other.title) return false
        if (body != other.body) return false
        if (signature != null) {
            if (other.signature == null) return false
            if (!signature.contentEquals(other.signature)) return false
        } else if (other.signature != null) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        result = 31 * result + id
        return result
    }


}