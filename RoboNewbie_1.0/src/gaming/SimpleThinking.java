/**
 * *****************************************************************************
 * RoboNewbie NaoTeam Humboldt
 *
 * @author Monika Domanska
 * @version 1.0
 * *****************************************************************************
 */
package gaming;

import agentIO.PerceptorInput;
import java.util.HashMap;
import keyframeMotion.KeyframeMotion;
import localFieldView.BallModel;
import localFieldView.GoalPostModel;
import localFieldView.LocalFieldView;
import util.FieldConsts;

/**
 * Thinking-Class for Agent_SimpleWalkToBall. This class takes action decisions
 * for a simple scenario: If the ball lies in front of the robot (shifted less
 * than 30° to the left or right side), then the robot should walk towards it.
 * Else the robot should turn left.
 *
 * This class uses informations about the ball provided by an object of class
 * LocalFieldView, and robot movements provided by class KeyframeMotion.
 * Therefore SimpleThinking depends on the functionality of these classes.
 *
 * Usage by the agent class: Call method decide() in each server cycle. Before
 * the call the LocalFieldView object has to be updated, and after the call the
 * KeyframeMotion object must execute the chosen movement.
 *
 * Class Agent_SimpleWalkToBall gives an example of correct integration and
 * usage.
 *
 */
public class SimpleThinking {

    private static final double TOLERANCE_ANGLE = Math.toRadians(10);
    private static final double TOLLERATED_DISTANCE = 0.7;

    protected KeyframeMotion motion;
    protected BallModel ball;
    private PerceptorInput percIn;

    // 0 = perform motion according to keyframe file test.txt
    // 1 = wait for wait_time cycles: is pose stable?
    // 2 = stand up from back
    // 3 = stand up from front
    protected int state = 0;
    private LocalFieldView localView;
  
    GoalPostModel oppGoalLPost, oppGoalRPost;
    
    /**
     * Constructor.
     *
     * @param localView Has to be already initialized.
     * @param motion Has to be already initialized.
     */
    public SimpleThinking(LocalFieldView localv, KeyframeMotion motion, PerceptorInput percIn) {
        this.ball = localv.getBall();
        localView = localv;
        this.motion = motion;
        this.percIn = percIn;
        
        HashMap<FieldConsts.GoalPostID, GoalPostModel> goalPosts = this.localView.getGoals();
        oppGoalLPost = goalPosts.get(FieldConsts.GoalPostID.G1R);
        oppGoalRPost = goalPosts.get(FieldConsts.GoalPostID.G2R);
    }

    /**
     * Decide, whether the robot should walk or turn to reach the ball and set
     * the chosen movement. If the ball is in front of the robot, it can just
     * walk forward, else it should turn left, and check its position relative
     * to the ball again.
     */
    boolean headdown = false;
    boolean isBallFOV = false, isStandUp = false, isWalking = false, isOnBack = false; 
    double lastBallAlpha = 0;
    double lastBallCoords = 0;
    
    public void decide() {    

        if (motion.ready()) {

            /* detecte si le robot est tombé */
            if (percIn.getAcc().getZ() < 7) {
                /* Si il est sur le dos */
                if (percIn.getAcc().getY() > 0) {
                    isStandUp = false;
                    isOnBack = true;
                } 
                /* si il est sur le ventre */
                else {
                    isStandUp = false;
                    isOnBack = false;                         // robot lies on the front
                }
            } else {
                isStandUp = true;
                isOnBack = false;
            }

            /* si la balle est visible */
            if (ball.isInFOVnow())
            {
                lastBallAlpha = ball.getCoords().getAlpha();
                isBallFOV = true;
            } 
            else 
            {
                isBallFOV = false;
            }      
            
            /* Si la position de la balle est dans l'angle de tolerance */
            if (isBallFOV == true && Math.abs(lastBallAlpha) < TOLERANCE_ANGLE) {
                
                if (ball.getCoords().getNorm() > TOLLERATED_DISTANCE)
                {
                    motion.setWalkForward();
                    isWalking = true;
                }
                else
                {
                    double goalLAplha = oppGoalLPost.getCoords().getAlpha();
                    double goalRAplha = oppGoalRPost.getCoords().getAlpha();
                    double ballAlpha = ball.getCoords().getAlpha();
                    
                                     
                    if ((oppGoalLPost.getCoords().getAlpha() <= ball.getCoords().getAlpha())
                    || (oppGoalRPost.getCoords().getAlpha() >= ball.getCoords().getAlpha()))
                    //if(Math.abs(ball.getCoords().getAlpha()) <  Math.toRadians(20))
                    {                    
                        if (isWalking) {
                            motion.setStopWalking();
                            isWalking = false;
                        } 
                        else {
                            if (oppGoalLPost.getCoords().getAlpha() <= ball.getCoords().getAlpha()) {
                                motion.setSideStepLeft();
                            } else {
                                motion.setSideStepRight();
                            }
                        }
                    }
                    else
                    {
                        motion.setWalkForward();
                        isWalking = true;    
                    }
                }
            }
            else if (isWalking) {
                motion.setStopWalking();
                isWalking = false;
            }
            /* ne marche pas et n'est pas debout */
            else if(isWalking==false && isStandUp==false)
            {
                if(isOnBack)
                {
                      motion.setStandUpFromBack();
                }
                else
                {
                      motion.setRollOverToBack();
                }
            }
            else //ne voit pas la balle
            {
                if (headdown == false) { 
                    motion.setTurnHeadDown();
                    headdown = true;
                }
                else
                {
                    /* choix du sens de rotation en fonction du dernier angle connu de la balle */
                    if(lastBallAlpha > 0)
                    {
                        motion.setTurnLeft();
                        headdown = false;
                    }
                    else
                    {
                        motion.setTurnRight();
                        headdown = false;
                    }
                    
                }
                
                /*
                      else {
                    motion.setTurnHeadUp();
                    headdown = false;
                }*/
            }
        }

  
    }

}
