package org.sert2521.bunnybots

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import org.sert2521.bunnybots.arm.DriveUntilTriggered
import org.sert2521.bunnybots.arm.addArmCommands
import org.sert2521.bunnybots.arm.initArm
import org.sert2521.bunnybots.arm.leftArmMotor
import org.sert2521.bunnybots.arm.setArmSpeed
import org.sert2521.bunnybots.claw.claw
import org.sert2521.bunnybots.claw.initClaw
import org.sert2521.bunnybots.drivetrain.addDrivetrainCommands
import org.sert2521.bunnybots.drivetrain.initDrivetrain
import org.sert2521.bunnybots.util.bucketSwitch
import org.strongback.Strongback
import org.strongback.command.Command
import org.strongback.command.CommandGroup

/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Robot : IterativeRobot() {
    override fun robotInit() {
        Strongback.logger().info("Robot starting...")
        Strongback.start()
        initDrivetrain()
        initClaw()
        initArm()
    }

    override fun autonomousInit() {
        Strongback.logger().info("Autonomous starting...")
        Strongback.start()
        claw.extend()
        addDefaultCommands()
        Strongback.submit(CommandGroup.runSequentially(
                CommandGroup.runSimultaneously(
                        DriveUntilTriggered(bucketSwitch),
                        object : Command(leftArmMotor) {
                            override fun execute(): Boolean {
                                setArmSpeed(0.2)
                                return bucketSwitch.isTriggered
                            }
                        }),
                object : Command(claw) {
                    override fun initialize() {
                        claw.retract()
                    }

                    override fun execute() = true
                },
                object : Command(leftArmMotor) {
                    override fun execute(): Boolean {
                        setArmSpeed(-0.176)
                        return false
                    }
                }
        ))
    }

    override fun teleopInit() {
        Strongback.logger().info("Teleop starting...")
        Strongback.start()
        claw.retract()
        addDefaultCommands()
    }

    override fun testPeriodic() = LiveWindow.run()

    override fun disabledInit() = Strongback.disable()

    private fun addDefaultCommands() {
        Strongback.switchReactor().onTriggered(bucketSwitch, Runnable {
            Strongback.logger().info("Bucket!")
        })
        addDrivetrainCommands()
        addArmCommands()
    }
}
