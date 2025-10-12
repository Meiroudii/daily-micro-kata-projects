import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

interface AddServerIntf extends Remote {
  double add(double d1, double d2) throws RemoteException;
}

class AddServerImpl extends UnicastRemoteObject implements AddServerIntf {
  public AddServerImpl() throws RemoteException {}
  public double add(double d1, double d2) throws RemoteException {
    return d1 + d2;
  }
}

public class HTTP23_1 {
  public static void main(String[] args) {
    try {
      AddServerImpl addServerImpl = new AddServerImpl();
      Naming.rebind("HTTP23_1", addServerImpl);
    } catch(Exception e) {
      System.out.println("Exception: "+e);
    }

    try {
       String addServerURL = "rmi://" + args[0] + "/index";
       AddServerIntf addServerIntf =
       (AddServerIntf)Naming.lookup(addServerURL);
       System.out.println("The first number is: " + args[1]);
       double d1 = Double.valueOf(args[1]).doubleValue();
       System.out.println("The second number is: " + args[2]);
       double d2 = Double.valueOf(args[2]).doubleValue();
       System.out.println("The sum is: " + addServerIntf.add(d1, d2));
     } catch(Exception e) {
       System.out.println("Exception: " + e);
     }
  }
}
