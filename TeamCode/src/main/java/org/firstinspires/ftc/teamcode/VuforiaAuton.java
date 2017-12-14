package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by connorespenshade on 12/14/17.
 */

@Autonomous(name = "Vuforia Test")
public class VuforiaAuton extends LinearOpMode {

    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia;

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    @Override
    public void runOpMode() throws InterruptedException {

        initMotors();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AR3UNn//////AAAAGWw+kvrhWEQ/n8JGCEaAl3lHjtRjVuwoY6pyIsg6Fc1fYaZbyySiQYqRxF29tMJufsu1X91zq+pfrk7qUb49WyQcME7VPLelNQj4I/8QV4nYk/8MqwfVFKqidKnYX2XGxyeLnH2wbOK04Ot9lpDYhBgjs7crF8Lbw/LEv21h54owkSRCsT4SuH0EKIztAlQfhUkwEtZyJ7QGzwtBJ3du06z4MMZcjPX56vPHf6ov4q+4yz2Z3i9RtDGAmIKxl+b31KX50XsZphctCQs5ig16Ho3Anux7E4dQ3/cq2dYGTwzUUiVl4sduiDjrU7O7rlu46X/F4CaTn6Iw7/PKBRe+/jC5iXwcin4U8cDWWxcsVagJ";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        relicTrackables.activate();

        waitForStart();

        while (opModeIsActive()) {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", pose);

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    telemetry.addData("VuMark ID", vuMark.name());

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot

                    double tY = trans.get(1);

                    tY = Math.round(tY * 100);
                    tY = tY/100;

                    if (vuMark.name() == "Left") {
                        if (tY < 1) {
                            leftFront.setPower(0.5);
                            leftBack.setPower(0.5);
                            rightFront.setPower(0.5);
                            rightBack.setPower(0.5);
                        }
                    } else if (vuMark.name() == "Center") {
                        if (tY < 2) {
                            leftFront.setPower(0.5);
                            leftBack.setPower(0.5);
                            rightFront.setPower(0.5);
                            rightBack.setPower(0.5);
                        }
                    } else /*RIGHT*/{
                        if (tY < 3) {
                            leftFront.setPower(0.5);
                            leftBack.setPower(0.5);
                            rightFront.setPower(0.5);
                            rightBack.setPower(0.5);
                        }
                    }

                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
            }

            idle();
        }

    }

    private void initMotors() {
        leftFront = hardwareMap.dcMotor.get(UniversalConstants.LEFT1NAME);
        leftBack = hardwareMap.dcMotor.get(UniversalConstants.LEFT2NAME);
        rightFront = hardwareMap.dcMotor.get(UniversalConstants.RIGHT1NAME);
        rightBack = hardwareMap.dcMotor.get(UniversalConstants.RIGHT2NAME);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}