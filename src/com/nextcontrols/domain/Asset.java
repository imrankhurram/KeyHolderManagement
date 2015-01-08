package com.nextcontrols.domain;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Asset implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***************** Persistent Properties ******************/

	private int id;
	private String assetName;
	private String imageName;
	private String fixture;
	private String subFixture;
	private String description;
	private Boolean showmeankineticenergy;
	private Boolean showbattery;
	private Boolean isAlert;
	private Boolean isDuplicate;
	private Boolean isolated;
	private Boolean hasIsolations;
	private SiteView branchView;
    //private AlarmAsset alarmasset;
    //private String alarmassetSubFixture;
    private int alarmasset_id;
	private String alarmassetSubFixture;
//    private Set <Input> inputs ;
//    private Set <Input> graphInputs ;
//    private Set <AssetInputRange> assetinputranges ;
//    
//    private Set <Output> outputs ;
//    private Set <Output> graphOutputs ;
//    private Set <AssetOutputRange> assetoutputranges ;
//    
//    private Set <Setpoint> setpoints ;
//    private Set <Setpoint> graphSetpoints ;
//    private Set <AssetSetpointRange> assetsetpointranges ;
//    
//    private Set <VirtualPoint> virtualpoints ;
//    private Set <VirtualPoint> graphVirtualPoints ;
//    private Set <AssetVirtualPointRange> assetvirtualpointranges ;
//    
//    private Set <TimeSchedule> timeschedules ;
//    private Set <AssetTimeScheduleRange> assettimescheduleranges ;
//    
//    private Set <DayType> daytypes ;
//    private Set <AssetDayTypeRange> assetdaytyperanges ;
    
    /***************** Constructors ****************************/
    public Asset(){}
    public Asset(String assetName){
//	    inputs = new LinkedHashSet <Input> ();
//	    graphInputs = new LinkedHashSet <Input> ();
//	    assetinputranges = new LinkedHashSet <AssetInputRange> ();
//	    
//	    outputs = new LinkedHashSet <Output> ();
//	    graphOutputs = new LinkedHashSet <Output> ();
//	    assetoutputranges = new LinkedHashSet <AssetOutputRange> ();
	    
//	    setpoints = new LinkedHashSet <Setpoint> ();
//	    graphSetpoints = new LinkedHashSet <Setpoint> ();
//	    assetsetpointranges = new LinkedHashSet <AssetSetpointRange> ();
	    
//	    virtualpoints = new LinkedHashSet <VirtualPoint> ();
//	    graphVirtualPoints = new LinkedHashSet <VirtualPoint> ();
//	    assetvirtualpointranges = new LinkedHashSet <AssetVirtualPointRange> ();
//	    
//	    timeschedules = new LinkedHashSet <TimeSchedule> ();
//	    assettimescheduleranges = new LinkedHashSet <AssetTimeScheduleRange> ();
//	    
//	    daytypes = new LinkedHashSet <DayType> ();
//	    assetdaytyperanges = new LinkedHashSet <AssetDayTypeRange> ();
	       
	    showmeankineticenergy = false;
	    showbattery = false;
	    
	    isAlert = false;
	    isDuplicate = false;
	    //fixture = "";
	   // subFixture = "Main Unit";
	    
	   // alarmasset_id = 0;
	   // alarmassetSubFixture = "Main Unit";
	    
   }

   
	public Asset(int id, String assetName, String description,
			Boolean showmeankineticenergy, Boolean showbattery,
			int alarmasset_id,String alarmassetSubFixture, Boolean isAlert, Boolean isDuplicate) {
		super();
		this.id = id;
		this.assetName = assetName;
		this.description = description;
		this.showmeankineticenergy = showmeankineticenergy;
		this.showbattery = showbattery;
		this.alarmasset_id = alarmasset_id;
		this.alarmassetSubFixture = alarmassetSubFixture;
		this.isAlert = isAlert;
		this.isDuplicate = isDuplicate;
//		inputs = new LinkedHashSet <Input> ();
//	    graphInputs = new LinkedHashSet <Input> ();
//	    assetinputranges = new LinkedHashSet <AssetInputRange> ();
//	    
//	    outputs = new LinkedHashSet <Output> ();
//	    graphOutputs = new LinkedHashSet <Output> ();
//	    assetoutputranges = new LinkedHashSet <AssetOutputRange> ();
//	    
//	    setpoints = new LinkedHashSet <Setpoint> ();
//	    graphSetpoints = new LinkedHashSet <Setpoint> ();
//	    assetsetpointranges = new LinkedHashSet <AssetSetpointRange> ();
//	    
//	    virtualpoints = new LinkedHashSet <VirtualPoint> ();
//	    graphVirtualPoints = new LinkedHashSet <VirtualPoint> ();
//	    assetvirtualpointranges = new LinkedHashSet <AssetVirtualPointRange> ();
//	    
//	    timeschedules = new LinkedHashSet <TimeSchedule> ();
//	    assettimescheduleranges = new LinkedHashSet <AssetTimeScheduleRange> ();
//	    
//	    daytypes = new LinkedHashSet <DayType> ();
//	    assetdaytyperanges = new LinkedHashSet <AssetDayTypeRange> ();

	}
	/***************** Accessors *******************************/
   /* public AlarmAsset getAlarmasset() {return alarmasset;}
	public void setAlarmasset(AlarmAsset alarmasset) {this.alarmasset = alarmasset;}
	
	public String getAlarmassetSubFixture() {return alarmassetSubFixture;}
	public void setAlarmassetSubFixture(String alarmassetSubFixture) {this.alarmassetSubFixture = alarmassetSubFixture;}*/
	
    public int getAlarmasset_id() {	return alarmasset_id;}
	public void setAlarmasset_id(int alarmasset_id) {this.alarmasset_id = alarmasset_id;}
	
	public String getAlarmassetSubFixture() {return alarmassetSubFixture;}
	public void setAlarmassetSubFixture(String alarmassetSubFixture) {this.alarmassetSubFixture = alarmassetSubFixture;}
	
	public SiteView getBranchView() { return branchView; }
    public void setBranchView(SiteView branchView) { this.branchView = branchView; }

    public String getAssetName() {return assetName;}
	public void setAssetName(String assetName) {this.assetName = assetName;}
	
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
        
	public String getImageName() {return imageName;}
	public void setImageName(String imageName) {this.imageName = imageName;}
	
	public String getFixture() {return fixture;}
	public void setFixture(String fixture) {this.fixture = fixture;}
	
	public String getSubFixture() {return subFixture;}
	public void setSubFixture(String subFixture) {this.subFixture = subFixture;}

    public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	
	public Boolean getShowmeankineticenergy() {return showmeankineticenergy;}
	public void setShowmeankineticenergy(Boolean showmeankineticenergy) {this.showmeankineticenergy = showmeankineticenergy;}
	
	public Boolean getShowbattery() {return showbattery;}
	public void setShowbattery(Boolean showbattery) {this.showbattery = showbattery;}
	
	public Boolean getIsAlert() {return isAlert;}
	public void setIsAlert(Boolean isAlert) {this.isAlert = isAlert;}
	
	public Boolean getIsDuplicate() {
		return isDuplicate;
	}
	public void setIsDuplicate(Boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}
//	public Set<Input> getInputs() {return inputs; }
//    public void setInputs(Set<Input> inputs) {this.inputs = inputs;}
//    public Set<Input> getGraphInputs() { return graphInputs; }
//    public void setGraphInputs(Set<Input> graphInputs) { this.graphInputs = graphInputs; }
//    public Set<AssetInputRange> getAssetinputranges() {return assetinputranges;}
//	public void setAssetinputranges(Set<AssetInputRange> assetinputranges) {this.assetinputranges = assetinputranges;}
//	
//    public Set<Output> getOutputs() {return outputs; }
//    public void setOutputs(Set<Output> outputs) {this.outputs = outputs;}
//    public Set<Output> getGraphOutputs() { return graphOutputs; }
//    public void setGraphOutputs(Set<Output> graphOutputs) { this.graphOutputs = graphOutputs; }
//    public Set<AssetOutputRange> getAssetoutputranges() {return assetoutputranges;}
//	public void setAssetoutputranges(Set<AssetOutputRange> assetoutputranges) {this.assetoutputranges = assetoutputranges;}
//	
//    public Set<Setpoint> getSetpoints() {return setpoints; }
//    public void setSetpoints(Set<Setpoint> setpoints) {this.setpoints = setpoints;}
//    public Set<Setpoint> getGraphSetpoints() { return graphSetpoints; }
//    public void setGraphSetpoints(Set<Setpoint> graphSetpoints) { this.graphSetpoints = graphSetpoints; }
//    public Set<AssetSetpointRange> getAssetsetpointranges() {return assetsetpointranges;}
//	public void setAssetsetpointranges(Set<AssetSetpointRange> assetsetpointranges) {this.assetsetpointranges = assetsetpointranges;}
//	
//    public Set<VirtualPoint> getVirtualpoints() {return virtualpoints;}
//	public void setVirtualpoints(Set<VirtualPoint> virtualpoints) {this.virtualpoints = virtualpoints;}
//	public Set<VirtualPoint> getGraphVirtualPoints() {return graphVirtualPoints;}
//	public void setGraphVirtualPoints(Set<VirtualPoint> graphVirtualPoints) { this.graphVirtualPoints = graphVirtualPoints; }
//	public Set<AssetVirtualPointRange> getAssetvirtualpointranges() {return assetvirtualpointranges;}
//	public void setAssetvirtualpointranges(Set<AssetVirtualPointRange> assetvirtualpointranges) {this.assetvirtualpointranges = assetvirtualpointranges;}
//	
//	public Set<TimeSchedule> getTimeschedules() {return timeschedules;}
//	public void setTimeschedules(Set<TimeSchedule> timeschedules) {this.timeschedules = timeschedules;}
//	public Set<AssetTimeScheduleRange> getAssettimescheduleranges() {return assettimescheduleranges;}
//	public void setAssettimescheduleranges(Set<AssetTimeScheduleRange> assettimescheduleranges) {this.assettimescheduleranges = assettimescheduleranges;}
//	
//	public Set<DayType> getDaytypes() {return daytypes;}
//	public void setDaytypes(Set<DayType> daytypes) {this.daytypes = daytypes;}
//	public Set<AssetDayTypeRange> getAssetdaytyperanges() {return assetdaytyperanges;}
//	public void setAssetdaytyperanges(Set<AssetDayTypeRange> assetdaytyperanges) {this.assetdaytyperanges = assetdaytyperanges;}
	
	/***************** Common Methods **************************/

    public String toString() {
        return assetName;
    }
	public Boolean getIsolated() {
		return isolated;
	}
	public void setIsolated(Boolean isolated) {
		this.isolated = isolated;
	}
	public Boolean getHasIsolations() {
		return hasIsolations;
	}
	public void setHasIsolations(Boolean hasIsolations) {
		this.hasIsolations = hasIsolations;
	}
    
    /***************** Business Methods ************************/
    
//    public Set <Input> listInputs() {
//        return Collections.unmodifiableSet(inputs);
//    }
//    
//    public boolean addInput(Input input) {
//        return inputs.add(input);
//    }
//    
//    public boolean removeInput(Input input) {
//    	Collection <AssetInputRange> temp = new Vector <AssetInputRange> ();
//    	for(AssetInputRange assetinputRange:listAssetInputRanges()) {
//    		if(assetinputRange.getInput()==input) {
//    			temp.add(assetinputRange);
//    		}
//    	}
//    	assetinputranges.removeAll(temp);
//        graphInputs.remove(input);
//        return inputs.remove(input);
//    }
//    
//    public void clearInputs() {
//    	assetinputranges.clear();
//        graphInputs.clear();
//        inputs.clear();
//    }
//    
//    public void updateInputs(Set <Input> updatedSensors) {;
//    	inputs.clear();
//    	inputs.addAll(updatedSensors);
//        graphInputs.retainAll(inputs);
//        
//    }
//    
//    public Set <Input> listGraphInputs() {
//        return Collections.unmodifiableSet(graphInputs);
//    }
//    
//    public Set <AssetInputRange> listAssetInputRanges() {
//        return assetinputranges;
//    }
//    public void addAssetInputRange(AssetInputRange temp){	
//    	assetinputranges.add(temp);
//    }
//    public void addAssetInputRanges(List <AssetInputRange> temp){	
//    	assetinputranges.addAll(temp);
//    }
//    public boolean removeAssetInputRanges(AssetInputRange temp) {
//        return assetinputranges.remove(temp);
//    }
//    
//    public boolean addGraphInputs(Input input) {
//        if (!inputs.contains(input)) {
//            throw new IllegalArgumentException("Input not associated with Asset");
//        }
//        return graphInputs.add(input);
//    }
//    
//    public boolean removeGraphInput(Input input) {
//        return graphInputs.remove(input);
//    }
//    
//    public void clearGraphInputs() {
//        graphInputs.clear();
//    }
//    public void clearAssetInputRanges() {
//        assetinputranges.clear();
//    }
//    
//    /***************** Business Methods Out************************/
//    public Set <Output> listOutputs() {
//        return Collections.unmodifiableSet(outputs);
//    }
//    
//    public boolean addOutput(Output output) {
//        return outputs.add(output);
//    }
//    
//    public boolean removeOutput(Output output) {
//    	Collection <AssetOutputRange> temp = new Vector <AssetOutputRange> ();
//    	for(AssetOutputRange assetoutputRange:listAssetOutputRanges()) {
//    		if(assetoutputRange.getOutput()==output) {
//    			temp.add(assetoutputRange);
//    		}
//    	}
//    	assetoutputranges.removeAll(temp);
//        graphOutputs.remove(output);
//        return outputs.remove(output);
//    }
//    
//    public void clearOutputs() {
//    	assetoutputranges.clear();
//        graphOutputs.clear();
//        outputs.clear();
//    }
//    
//    public void updateOutputs(Set <Output> updatedOutputs) {;
//    	outputs.clear();
//    	outputs.addAll(updatedOutputs);
//        graphOutputs.retainAll(outputs);
//        
//    }
//    
//    public Set <Output> listGraphOutputs() {
//        return Collections.unmodifiableSet(graphOutputs);
//    }
//    
//    public Set <AssetOutputRange> listAssetOutputRanges() {
//        return assetoutputranges;
//    }
//    public void addAssetOutputRange(AssetOutputRange temp){	
//    	assetoutputranges.add(temp);
//    }
//    public boolean removeAssetOutputRanges(AssetOutputRange temp) {
//        return assetoutputranges.remove(temp);
//    }
//    
//    public boolean addGraphOutputs(Output output) {
//        if (!outputs.contains(output)) {
//            throw new IllegalArgumentException("Output not associated with Asset");
//        }
//        return graphOutputs.add(output);
//    }
//    
//    public boolean removeGraphOutput(Output output) {
//        return graphOutputs.remove(output);
//    }
//    
//    public void clearGraphOutputs() {
//        graphOutputs.clear();
//    }
//    public void clearAssetOutputRanges() {
//        assetoutputranges.clear();
//    }
//   
//    /***************** Business Methods Setpoint************************/
//    public Set <Setpoint> listSetpoints() {
//        return Collections.unmodifiableSet(setpoints);
//    }
//    
//    public boolean addSetpoint(Setpoint setpoint) {
//        return setpoints.add(setpoint);
//    }
//    
//    public boolean removeSetpoint(Setpoint setpoint) {
//    	Collection <AssetSetpointRange> temp = new Vector <AssetSetpointRange> ();
//    	for(AssetSetpointRange assetsetpointRange:listAssetSetpointRanges()) {
//    		if(assetsetpointRange.getSetpoint()==setpoint) {
//    			temp.add(assetsetpointRange);
//    		}
//    	}
//    	assetsetpointranges.removeAll(temp);
//        graphSetpoints.remove(setpoint);
//        return setpoints.remove(setpoint);
//    }
//    
//    public void clearSetpoints() {
//    	assetsetpointranges.clear();
//        graphSetpoints.clear();
//        setpoints.clear();
//    }
//    
//    public void updateSetpoints(Set <Setpoint> updatedSetpoints) {
//    	setpoints.clear();
//    	setpoints.addAll(updatedSetpoints);
//        graphSetpoints.retainAll(setpoints);
//        
//    }
//    
//    public Set <Setpoint> listGraphSetpoints() {
//        return Collections.unmodifiableSet(graphSetpoints);
//    }
//    
//    public Set <AssetSetpointRange> listAssetSetpointRanges() {
//        return assetsetpointranges;
//    }
//    public void addAssetSetpointRange(AssetSetpointRange temp){	
//    	assetsetpointranges.add(temp);
//    }
//    
//    public void addAssetSetpointRanges(List <AssetSetpointRange> temp){	
//    	assetsetpointranges.addAll(temp);
//    }
//    public boolean removeAssetSetpointRanges(AssetSetpointRange temp) {
//        return assetsetpointranges.remove(temp);
//    }
//    
//    public boolean addGraphSetpoints(Setpoint setpoint) {
//        if (!setpoints.contains(setpoint)) {
//            throw new IllegalArgumentException("Setpoint not associated with Asset");
//        }
//        return graphSetpoints.add(setpoint);
//    }
//    
//    public boolean removeGraphSetpoint(Setpoint setpoint) {
//        return graphSetpoints.remove(setpoint);
//    }
//    
//    public void clearGraphSetpoints() {
//        graphSetpoints.clear();
//    }
//    public void clearAssetSetpointRanges() {
//        assetsetpointranges.clear();
//    }
//    
//    /***************** Business Methods VirtualPoint************************/
//    public Set <VirtualPoint> listVirtualPoints() {
//        return Collections.unmodifiableSet(virtualpoints);
//    }
//    
//    public boolean addVirtualPoint(VirtualPoint virtualpoint) {
//        return virtualpoints.add(virtualpoint);
//    }
//    
//    public boolean removeVirtualPoint(VirtualPoint virtualpoint) {
//    	Collection <AssetVirtualPointRange> temp = new Vector <AssetVirtualPointRange> ();
//    	for(AssetVirtualPointRange assetvirtualpointRange:listAssetVirtualPointRanges()) {
//    		if(assetvirtualpointRange.getVirtualpoint()==virtualpoint) {
//    			temp.add(assetvirtualpointRange);
//    		}
//    	}
//    	assetvirtualpointranges.removeAll(temp);
//        graphSetpoints.remove(virtualpoint);
//        return setpoints.remove(virtualpoint);
//    }
//    
//    public void clearVirtialPoints() {
//    	assetvirtualpointranges.clear();
//        graphVirtualPoints.clear();
//        virtualpoints.clear();
//    }
//    
//    public void updateVirtualPoints(Set <VirtualPoint> updatedVirtualPoints) {
//    	virtualpoints.clear();
//    	virtualpoints.addAll(updatedVirtualPoints);
//        graphVirtualPoints.retainAll(virtualpoints);
//    }
//    
//    public Set <VirtualPoint> listGraphVirtualPoints() {
//        return Collections.unmodifiableSet(graphVirtualPoints);
//    }
//    
//    public Set <AssetVirtualPointRange> listAssetVirtualPointRanges() {
//        return assetvirtualpointranges;
//    }
//    public void addAssetVirtualPointRange(AssetVirtualPointRange temp){	
//    	assetvirtualpointranges.add(temp);
//    }
//    
//    public void addAssetVirtualPointRanges(List <AssetVirtualPointRange> temp){	
//    	assetvirtualpointranges.addAll(temp);
//    }
//    
//    public boolean removeAssetVirtualPointRanges(AssetVirtualPointRange temp) {
//        return assetvirtualpointranges.remove(temp);
//    }
//    
//    public boolean addGraphVirtalPoints(VirtualPoint virtualpoint) {
//        if (!virtualpoints.contains(virtualpoint)) {
//            throw new IllegalArgumentException("VirtualPoint not associated with Asset");
//        }
//        return graphVirtualPoints.add(virtualpoint);
//    }
//    
//    public boolean removeGraphVirtualPoints(VirtualPoint virtualpoint) {
//        return graphVirtualPoints.remove(virtualpoint);
//    }
//    
//    public void clearGraphVirtualPoints() {
//        graphVirtualPoints.clear();
//    }
//    public void clearAssetVirtualPointRanges() {
//        assetvirtualpointranges.clear();
//    }
//    
//    /***************** Business Methods VirtualPoint************************/
//    public Set <TimeSchedule> listTimeSchedules() {
//        return Collections.unmodifiableSet(timeschedules);
//    }
//    
//    public boolean addTimeSchedule(TimeSchedule timeschedule) {
//        return timeschedules.add(timeschedule);
//    }
//    public boolean removeTimeSchedule(TimeSchedule timeschedule) {
//    	
//    	return timeschedules.remove(timeschedule);
//    }
//    public void clearTimeSchedules() {
//        timeschedules.clear();
//    }
//    public void updateTimeSchedules(Set <TimeSchedule> updatedTimeSchedules) {
//    	timeschedules.clear();
//    	timeschedules.addAll(updatedTimeSchedules);
//    }
	
}
