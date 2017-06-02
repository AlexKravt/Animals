package p4.guide_animals.model;

public class OptionsItem {

	private String title;
	private int icon;

	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;

	public OptionsItem(){}

    public OptionsItem(String title)
    {
        this.title = title;
    }
	public OptionsItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}

	public OptionsItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public String getTitle(){
		return this.title;
	}


	public int getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
