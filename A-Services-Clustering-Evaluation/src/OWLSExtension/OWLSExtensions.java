package OWLSExtension;

import impl.owl.WrappedIndividual;
import impl.owls.profile.ProfileImpl;
import impl.owls.service.ServiceImpl;

import org.mindswap.exceptions.ConversionException;
import org.mindswap.owl.OWLConfig;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLObject;
import org.mindswap.owl.OWLObjectConverter;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;
import org.mindswap.owls.vocabulary.OWLS;
import org.mindswap.owls.Ivocabulary.OWLS2;

/**
 * An example demonstrating the extensibility features of the API. 
 * 
 * This example shows how the default Service implementation can be extended to support
 * custom defined extensions of the . 
 *   
 * 
 * @author Salma Najar
 *
 */
//public class OWLSExtensions implements Runnable {
	public class OWLSExtensions{
    
    public void run() {
    	
        // Override the default Profile converter to return ExtendedProfile descriptions
        OWLConfig.addConverter(Profile.class, new ExtendedProfileConverter());
        OWLConfig.addConverter(Service.class, new ExtendedServiceConverter());
        OWLConfig.addConverter(Intention.class, new IntentionConverter());
        OWLConfig.addConverter(Verb.class, new VerbConverter());
        OWLConfig.addConverter(Target.class, new TargetConverter());    
       
    }
    
    /**
     * An extension to existing Profile implmentation to return contact information.
     *
     */
    public class ExtendedProfile extends ProfileImpl {
        /**
         * Wrap the given OWLIndividual as a profile instance.
         * 
         * @param ind
         */
        public ExtendedProfile(OWLIndividual ind) {
            super(ind);
        }
        
    	public String getContext() {
    	
    	    return getPropertyAsString(OWLS2.Profile.context);
    	}
    	
    	public Intention getIntention() {
  		
    		return (Intention) getPropertyAs(OWLS2.Profile.hasIntention, Intention.class);
    	}   	
    }
    
    
    
    public  class ExtendedService extends ServiceImpl {
        /**
         * Wrap the given OWLIndividual as a service instance.
         * 
         * @param ind
         */
        public ExtendedService(OWLIndividual ind) {
            super(ind);
        }
        
    	public Intention getIntention() {
    		
    		return (Intention) getPropertyAs(OWLS2.Service.satisfies, Intention.class);
    	}
    }
    
    
    public class Intention extends WrappedIndividual  {
        /**
         * @param ind
         */
        public Intention(OWLIndividual ind) {
            super(ind);
        }
        
    	public Verb getVerb() {
    		return (Verb) getPropertyAs(OWLS2.Intention.hasVerb, Verb.class);
    	}
        
    	public Target getTarget() {
    		return (Target) getPropertyAs(OWLS2.Intention.hasTarget, Target.class);
    	}
       
    }
    
    public class Verb extends WrappedIndividual  {
        /**
         * @param ind
         */
        public Verb(OWLIndividual ind) {
            super(ind);
        }
        
        public String getSensVerb() {
    	    return getPropertyAsString(OWLS2.Verb.sensVerb);
    	}

    }
    
    public class Target extends WrappedIndividual  {
        /**
         * @param ind
         */
        public Target (OWLIndividual ind) {
            super(ind);
        }       
    }
   
    /**
     * A simple converter that will wrap existing OWLIndividuals as ExtendedProfile objects.
     * Very similar to the default Profile converter. 
     *
     */
    public class ExtendedProfileConverter implements OWLObjectConverter {
        @Override
		public boolean canCast(OWLObject object) {

        	return (object instanceof OWLIndividual) &&
                                   ((OWLIndividual) object).isType(OWLS.Profile.Profile);
        }

        @Override
		public OWLObject cast(OWLObject object) {
                    if(!canCast(object))
                        throw new ConversionException("OWLObject " + object + " cannot be cast to Profile class");
                
            return new ExtendedProfile((OWLIndividual) object);
        }

        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                ((OWLIndividual) object).addType(OWLS.Profile.Profile);
            
            return cast(object);
        }        
    }
    
    
    
    public class IntentionConverter implements OWLObjectConverter {
        @Override
		public boolean canCast(OWLObject object) {
                    
        	return (object instanceof OWLIndividual) &&
                                   ((OWLIndividual) object).isType(OWLS2.Intention.Intention);
        }

        @Override
		public OWLObject cast(OWLObject object) {
                    if(!canCast(object))
                        throw new ConversionException("OWLObject " + object + " cannot be cast to Intention class");
                
            return new Intention((OWLIndividual) object);
        }     
        public OWLObject convert(OWLObject object) {
                ((OWLIndividual) object).addType(OWLS2.Intention.Intention); 
            
            return cast(object);
        }        
    }
    
    
    public class VerbConverter implements OWLObjectConverter {
        @Override
		public boolean canCast(OWLObject object) {
                     
        	return (object instanceof OWLIndividual) &&
                                   ((OWLIndividual) object).isType(OWLS2.Verb.Verb);
        }

        @Override
		public OWLObject cast(OWLObject object) {
                    if(!canCast(object))
                        throw new ConversionException("OWLObject " + object + " cannot be cast to Verb class");
                
            return new Verb((OWLIndividual) object);
        }     
        public OWLObject convert(OWLObject object) {
                ((OWLIndividual) object).addType(OWLS2.Verb.Verb); 
            
            return cast(object);
        }        
    }
    
    public class TargetConverter implements OWLObjectConverter {
        @Override
		public boolean canCast(OWLObject object) {
               
        	return (object instanceof OWLIndividual) &&
                                   ((OWLIndividual) object).isType(OWLS2.Target.Target);
        }

        @Override
		public OWLObject cast(OWLObject object) {
                    if(!canCast(object))
                        throw new ConversionException("OWLObject " + object + " cannot be cast to Target class");
                
            return new Target((OWLIndividual) object);
        }     
        public OWLObject convert(OWLObject object) {
                ((OWLIndividual) object).addType(OWLS2.Target.Target); 
            
            return cast(object);
        }        
    }
    
    
    /**
     * A simple converter that will wrap existing OWLIndividuals as ExtendedService objects.
     * Very similar to the default Profile converter. 
     *
     */
    public class ExtendedServiceConverter implements OWLObjectConverter {
        @Override
		public boolean canCast(OWLObject object) {
                        return (object instanceof OWLIndividual) &&
                                   ((OWLIndividual) object).isType(OWLS.Service.Service);
        }

        @Override
		public OWLObject cast(OWLObject object) {
                    if(!canCast(object))
                        throw new ConversionException("OWLObject " + object + " cannot be cast to Service class");
                
            return new ExtendedService((OWLIndividual) object);
        }

        public OWLObject convert(OWLObject object) {
            if(!canCast(object))
                ((OWLIndividual) object).addType(OWLS.Service.Service);
            
            return cast(object);
        }        
    }
}