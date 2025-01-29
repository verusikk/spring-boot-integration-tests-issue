package com.azul.avd.kbs.app.system.batch

import org.springframework.batch.item.ItemReader
import java.util.concurrent.ConcurrentLinkedQueue

open class QueueItemReader<T>(
    iterable: Collection<T>,
): ItemReader<T> {

    private val queue = ConcurrentLinkedQueue(iterable)

    override fun read(): T? {
        return queue.poll()
    }
}