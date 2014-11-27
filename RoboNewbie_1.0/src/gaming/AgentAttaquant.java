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

    String _team = "Testteam";
    String _id = "test";
      double _beamX = 0;
      double _beamY = 0;
      double _beamRot = -90;

      if(args.length > 0)
      {
         _team = args[0];
         _id = args[1];
         _beamX = Double.parseDouble(args[2]);
         _beamY = Double.parseDouble(args[3]);
         _beamRot = -90;
      }

      if(args.length > 0)
      {
         _team = args[0];
        _id = args[1];
        _beamX = Double.parseDouble(args[2]);
        _beamY = Double.parseDouble(args[3]);
        _beamRot = -90;
      }
      
    Agent_SimpleWalkToBall agent = new Agent_SimpleWalkToBall();
    
    agent.init(_id, _team, _beamX, _beamY, _beamRot); 
    
    agent.run();
    agent.log.printLog();
    System.out.println("Agent stopped.");
  }  
    
}
