package at.ac.tuwien.sepm.individual.assignment.vehiclerental

import kotlin.experimental.and

infix fun Byte.shr(byte: Int): Int = this.toInt() shr byte
fun ByteArray.toHexString(): String =
    with(StringBuilder(this.size * 2)) {
        val hexCode = "0123456789ABCDEF"
        this@toHexString.forEach {
            append(hexCode[it shr 4 and 0xF])
            append(hexCode[(it and 0xF).toInt()])
        }
        return@with toString()
    }