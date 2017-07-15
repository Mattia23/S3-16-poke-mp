package distributed

import java.util.concurrent.ConcurrentHashMap

object ConnectedUsers extends ConcurrentHashMap[Int, User]
