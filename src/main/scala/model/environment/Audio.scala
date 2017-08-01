package model.environment

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

trait Audio {
  def play(): Unit

  def stop(): Unit

  def loop(): Unit
}

object Audio{
  def apply(song: String): Audio = new AudioImpl(song: String)
}

class AudioImpl(song: String) extends  Audio{

  private val audio: AudioInputStream = AudioSystem getAudioInputStream getClass.getResource(song)
  private val clip: Clip = AudioSystem.getClip
  clip open audio
  play()

  override def play(): Unit = clip.start()

  override def stop(): Unit = {
    clip.stop()
    clip setMicrosecondPosition 0
  }

  override def loop(): Unit = clip loop Clip.LOOP_CONTINUOUSLY
}