/*******************************************************************************
*  RoboNewbie
* NaoTeam Humboldt
* @author Monika Domanska
* @version 1.0
*******************************************************************************/

package gaming;

import agentIO.PerceptorInput;
import keyframeMotion.KeyframeMotion;
import localFieldView.BallModel;
import localFieldView.LocalFieldView;

/**
 * Thinking-Class for Agent_SimpleWalkToBall.
 * This class takes action decisions for a simple scenario: If the ball lies in
 * front of the robot (shifted less than 30° to the left or right side), then 
 * the robot should walk towards it. Else the robot should turn left.
 * 
 * This class uses informations about the ball provided by an object of class
 * LocalFieldView, and robot movements provided by class KeyframeMotion. 
 * Therefore SimpleThinking depends on the functionality of these classes.
 * 
 * Usage by the agent class:
 * Call method decide() in each server cycle. 
 * Before the call the LocalFieldView object has to be updated, and after the call
 * the KeyframeMotion object must execute the chosen movement. 
 * 
 * Class Agent_SimpleWalkToBall gives an example of correct integration and 
 * usage. 
 * 
 */
public class SimpleThinking {

  private static final double TOLERANCE_ANGLE = Math.toRadians(30);
  
  protected KeyframeMotion motion;
  protected BallModel ball;
  private PerceptorInput percIn;

  // 0 = perform motion according to keyframe file test.txt
  // 1 = wait for wait_time cycles: is pose stable?
  // 2 = stand up from back
  // 3 = stand up from front
  protected int state = 0;

  protected boolean robotIsWalking = false;

  /**
   * Constructor. 
   * 
   * @param localView Has to be already initialized. 
   * @param motion Has to be already initialized. 
   */
  public SimpleThinking(LocalFieldView localView, KeyframeMotion motion, PerceptorInput percIn) {
    this.ball = localView.getBall();
    this.motion = motion;
    this.percIn = percIn;
  }
    
  /**
   * Decide, whether the robot should walk or turn to reach the ball and set the
   * chosen movement. 
   * If the ball is in front of the robot, it can just walk forward, else it 
   * should turn left, and check its position relative to the ball again. 
   */
  public void decide() {
    
      boolean headdown = false;
         
    // Take care not to interrupt an actually executed movement.
    // This has to be checked always when using class KeyframeMotion. 
    if (motion.ready()) {
      if (percIn.getAcc().getZ() < 7) {
        if (percIn.getAcc().getY() > 0) {
          state = 2;    // robot lies on the back
        } else {
          state = 3;                               // robot lies on the front
        }
      }
      switch (state) {
        case 0:
            // If the ball lies in front of the robot, walk towards it. 
            if (ball.isInFOVnow()
                && (Math.abs(ball.getCoords().getAlpha())) < TOLERANCE_ANGLE) {
              motion.setWalkForward();
              robotIsWalking = true;
            }
      
            // If the ball lies somewhere else, first stop the walking to
            // prepare for the turning, and then turn left.  
            else if (robotIsWalking) 
            {
              motion.setStopWalking();
              robotIsWalking = false;
            }
            else {

                if(headdown == false)
                {
                  motion.setTurnHeadDown();
                  headdown = true;
                  headdown = false;
                }
                else
                {
                  motion.setTurnHeadDown();   
                  headdown = false;
                  headdown = true;
                }
            }
          state = 0;
          break;
        case 1:
          break;
        case 2:
          motion.setStandUpFromBack();
          state = 0;
          break;
        case 3:
          motion.setRollOverToBack();
          state = 0;    
          break;
      }
    }
  }
  
}
