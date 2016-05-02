/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mkws;

///**
// *
// * @author oerden
// */
//public class DeviceTypes {
//    
//    int mk = 1;
//    int mp = 2;
//    int other = -1;
//    public enum TYPE{
//        MKS,MPS,OTHER
//    }        
//}

public enum DeviceTypes
{
  MK("Desktop",1),
  MP("Mobile Phone",2),
  OTHER("Other Device",-1);
  
 
  /**
   *  Private variable definitions.
   */
  private int id;
  private String description;
 
  /**
   *  Constructs an instance with a cost and description. 
   */
  private DeviceTypes(String description, int id) {
    this.description = description;
    this.id = id; }
 
  /**
   *  Returns the cost field of an Apple Computer. 
   */
  public int getId() {
    return this.id; }
 
  /**
   *  Returns the description field of an Apple Computer. 
   */
  public String getName() {
    return description; }
 
  /**
   *  Returns the description field of an Apple Computer. 
   */
    /**
   *  Returns the equality of between two AppleComputer Enum types. 
   */
  public boolean equals(DeviceTypes dt) {
    // First comparision on primitives and second on String instances.
    if ((this.id == dt.getId()) && (this.description.equals(dt.getName())))
      return true;
    else
      return false; }
   
}