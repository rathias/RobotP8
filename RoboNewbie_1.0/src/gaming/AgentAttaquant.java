/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gaming;

/**
 *
 * @author rathias
 */
public class AgentAttaquant extends Agent_SimpleWalkToBall {
      
    public static void main(String args[]) {
    
    String _team = args[0];
    String _id = args[1];
    double _beamX = Double.parseDouble(args[2]);
    double _beamY = Double.parseDouble(args[3]);
    double _beamRot = -90;
    
    Agent_SimpleWalkToBall agent = new Agent_SimpleWalkToBall();
    
    agent.init(_id, _team, _beamX, _beamY, _beamRot); 
    
    agent.run();
    agent.log.printLog();
    System.out.println("Agent stopped.");
  }  
    
}
