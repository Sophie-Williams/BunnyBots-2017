package org.sert2521.bunnybots.driving

import org.sert2521.bunnybots.util.FRONT_LEFT_MOTOR
import org.sert2521.bunnybots.util.FRONT_RIGHT_MOTOR
import org.sert2521.bunnybots.util.REAR_LEFT_MOTOR
import org.sert2521.bunnybots.util.REAR_RIGHT_MOTOR
import org.sert2521.bunnybots.util.leftJoystick
import org.sert2521.bunnybots.util.rightJoystick
import org.sert2521.bunnybots.util.scaledPitch
import org.sert2521.bunnybots.util.scaledRoll
import org.strongback.Strongback
import org.strongback.command.Command
import org.strongback.components.Motor
import org.strongback.hardware.Hardware
import java.util.function.Supplier
import org.strongback.drive.TankDrive as Drive

val drive = Drive(
        Motor.compose(
                Hardware.Motors.talonSRX(FRONT_LEFT_MOTOR),
                Hardware.Motors.talonSRX(FRONT_RIGHT_MOTOR)
        ),
        Motor.compose(
                Hardware.Motors.talonSRX(REAR_LEFT_MOTOR),
                Hardware.Motors.talonSRX(REAR_RIGHT_MOTOR)
        )
)
private val defaultDrive: Command
    get() = ArcadeDrive(drive, leftJoystick.scaledPitch, leftJoystick.scaledRoll)

fun initDrivetrain() {
    Strongback.submit(defaultDrive)
    Strongback.switchReactor().apply {
        onTriggeredSubmit(leftJoystick.thumb, Supplier {
            ArcadeDrive(drive, leftJoystick.scaledPitch, leftJoystick.scaledRoll)
        })
        onTriggeredSubmit(rightJoystick.thumb, Supplier {
            TankDrive(drive, leftJoystick.scaledPitch, rightJoystick.scaledPitch)
        })
    }
}
