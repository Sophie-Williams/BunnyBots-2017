package org.sert2521.bunnybots.arm

import edu.wpi.first.wpilibj.command.Command
import org.sertain.hardware.Talon

abstract class TalonResetCommandBase(
        private val talon: Talon,
        timeout: Double = 0.0
) : Command(timeout) {
    override fun end() = talon.stopMotor()
}
