package com.examples.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoList extends Activity {
	
  static final private int ADD_NEW_TODO = Menu.FIRST;
  static final private int REMOVE_TODO = Menu.FIRST + 1;
  static final private int MENU_ITEM3 = Menu.FIRST + 2;
  static final private int MENU_ITEM4 = Menu.FIRST + 3;
  static final private String DEFAULT_EDIT_TEXT_PROMPT = "Enter your todo list item here";
  static final private String DTAG = "DD-DEBUG";
  
  private boolean addingNew = false;
  private ArrayList<String> todoItems;
  private ListView myListView;
  private EditText myEditText;
  private ArrayAdapter<String> aa;

  /** Called when the activity is first created. */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(DTAG,"Inside onCreate");
    
    // Inflate your view
    setContentView(R.layout.main);
      
    // Get references to UI widgets
    myListView = (ListView)findViewById(R.id.myListView);
    myEditText = (EditText)findViewById(R.id.myEditText);

    todoItems = new ArrayList<String>();
    
    int resID = R.layout.todolist_item;
    aa = new ArrayAdapter<String>(this, resID, todoItems);
    myListView.setAdapter(aa);
        
    myEditText.setOnKeyListener(new OnKeyListener() {
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
          if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            todoItems.add(0, myEditText.getText().toString());
            myEditText.setText("");
            myEditText.setHint(DEFAULT_EDIT_TEXT_PROMPT);
            aa.notifyDataSetChanged();
            cancelAdd();
            return true; 
          }
        return false;
      }
    });
    
    registerForContextMenu(myListView);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    Log.d(DTAG,"Populating the option Menu");

    // Create and add new menu items.
    MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE,
                                R.string.add_new);
    MenuItem itemRem = menu.add(0, REMOVE_TODO, Menu.NONE,
                                R.string.remove);

    // Added Menu Item# 3 and Item#4 
    MenuItem itemOne = menu.add(0, MENU_ITEM3, Menu.NONE,
            R.string.menuitem3);
    MenuItem itemTwo = menu.add(0, MENU_ITEM4, Menu.NONE,
            R.string.menuitem4);
    
    // Sub Menu Items
    SubMenu sub = menu.addSubMenu(0,0, Menu.NONE, R.string.submenu1);
    sub.setHeaderIcon(R.drawable.star);
    sub.setIcon(R.drawable.star);
    MenuItem submenuitem1 = sub.add(0, 0, Menu.NONE,
            "submenuitem1");
    submenuitem1.setIcon(R.drawable.star);

    
    // Assign icons
    itemAdd.setIcon(R.drawable.add_new_item);
    itemRem.setIcon(R.drawable.remove_item);
    itemOne.setIcon(R.drawable.star);
    itemTwo.setIcon(R.drawable.star);

    // Allocate shortcuts to each of them.
    itemAdd.setShortcut('0', 'a');
    itemRem.setShortcut('1', 'r');
    itemOne.setShortcut('2', '2');
    itemTwo.setShortcut('3', '3');

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    Log.d(DTAG,"onPrepareOptionMenu...");

    int idx = myListView.getSelectedItemPosition();

    String removeTitle = getString(addingNew ? 
                                   R.string.cancel : R.string.remove);

    MenuItem removeItem = menu.findItem(REMOVE_TODO);
    removeItem.setTitle(removeTitle);
    removeItem.setVisible(addingNew || idx > -1);

    return true;
  }
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, 
                                  View v, 
                                  ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    Log.d(DTAG,"onCreateContextMenu...");
    menu.setHeaderTitle("Selected To Do Item");
    menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    int index = myListView.getSelectedItemPosition();

    Log.d(DTAG,"optionMenuItemSelected: "+index);

    switch (item.getItemId()) {
      case (REMOVE_TODO): {
        if (addingNew) {
          cancelAdd();
        } 
        else {
          removeItem(index);
        }
        return true;
      }
      case (ADD_NEW_TODO): { 
        addNewItem();
        return true; 
      }
    }

    return false;
  }
  
  @Override
  public boolean onContextItemSelected(MenuItem item) {  
    super.onContextItemSelected(item);
    Log.d(DTAG,"onContextMenuItemSelected...");
    switch (item.getItemId()) {
      case (REMOVE_TODO): {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = menuInfo.position;

        removeItem(index);
        return true;
      }
    }
    return false;
  }
  
  private void cancelAdd() {
    addingNew = false;
    myEditText.setVisibility(View.GONE);
    Log.d(DTAG,"Add cancelled...");
  }

  private void addNewItem() {
    addingNew = true;
    myEditText.setVisibility(View.VISIBLE);
    myEditText.requestFocus();
    // Default text that will appear in Edit Text
    myEditText.setText("");
    myEditText.setHint(DEFAULT_EDIT_TEXT_PROMPT);
    Log.d(DTAG,"Adding new item...");
  }

  private void removeItem(int _index) {
    todoItems.remove(_index);
    aa.notifyDataSetChanged(); 
    Log.d(DTAG,"Item Removed..."+ _index);
  }
}