package model

/**
 * Basic model
 */
class DNA(private val strand: String) {

    val id = hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DNA
        if (id != other.id) return false
        return true
    }

    override fun hashCode() =  strand.hashCode() * 31

    override fun toString(): String {
        return strand
    }
}