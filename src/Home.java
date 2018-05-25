import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.io.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;

import java.text.*;

class Home extends JFrame implements TreeWillExpandListener,TreeSelectionListener,MouseListener, MouseMotionListener
{
 private JFrame frame = new JFrame("/Home/");
 private Container con = null;
 private JSplitPane pMain=new JSplitPane();
 private JScrollPane pLeft=null;
 private JPanel pRight=new JPanel(new BorderLayout());
 private DefaultMutableTreeNode root = new DefaultMutableTreeNode("...");
 private JTree tree;
 private JPanel pNorth=new JPanel();
 JPanel pSouth = new JPanel();
 private JTextField pathText=new JTextField();
 JPanel contentPane = new JPanel();
 JLabel la;
 JComboBox ia = new JComboBox();
 JPanel ias = new JPanel();
 JLabel iass = new JLabel("file manager");
 
 Home(){
  init();
  start();
  frame.setSize(800,600);
  frame.setVisible(true);
 }

 void init(){
  pMain.setResizeWeight(1);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  con = frame.getContentPane();
  con.setLayout(new BorderLayout());
  pathText.setPreferredSize(new Dimension(400,20));
  con.add(pNorth,"North");
  con.add(pSouth, "South");
  ia.addItem("한국어");
  ia.addItem("English");
  ias.add(iass);
  ias.add(ia);
  con.add(ias,BorderLayout.SOUTH);
  File file=new File("");
  File list[]=file.listRoots();
  DefaultMutableTreeNode temp;

  for(int i=0;i<list.length;++i)
  {
   temp=new DefaultMutableTreeNode(list[i].getPath());
   temp.add(new DefaultMutableTreeNode("없음"));
   root.add(temp);
  }
  tree=new JTree(root);
  pLeft=new JScrollPane(tree);
  pMain.setDividerLocation(150);
  pMain.setLeftComponent(pLeft);
  pMain.setRightComponent(pRight);
  con.add(pMain);
  
  
 }

 void start()
 {
  tree.addTreeWillExpandListener(this);
  tree.addTreeSelectionListener(this);
 }

 public static void main(String args[]){
  JFrame.setDefaultLookAndFeelDecorated(true);
  new Home();
 }

 String getPath(TreeExpansionEvent e)
 {
  StringBuffer path=new StringBuffer();
  TreePath temp=e.getPath(); 
  Object list[]=temp.getPath();
  for(int i=0;i<list.length;++i)
  {
   if(i>0)
   {
    path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
   }
  }
  return path.toString();
 }
 
 String getPath(TreeSelectionEvent e)
 {
  StringBuffer path=new StringBuffer();
  TreePath temp=e.getPath(); 
  Object list[]=temp.getPath();
  for(int i=0;i<list.length;++i)
  {
   if(i>0)
   {
    path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
   }
  }
  return path.toString();
 }
 
 public void treeWillCollapse(TreeExpansionEvent event){}
 
 public void treeWillExpand(TreeExpansionEvent e)
 {
  if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("내컴퓨터")){}
  else
  {
   try{
    DefaultMutableTreeNode parent=(DefaultMutableTreeNode)e.getPath().getLastPathComponent();
    File tempFile=new File(getPath(e));
    File list[]=tempFile.listFiles();
    DefaultMutableTreeNode tempChild;
    for(File temp:list)
    {
     if(temp.isDirectory() && !temp.isHidden())
     {
      tempChild=new DefaultMutableTreeNode(temp.getName());
      if(true)
      {
       File inFile=new File(getPath(e)+temp.getName()+"\\");
       File inFileList[]=inFile.listFiles();
       for(File inTemp:inFileList)
       {
        if(inTemp.isDirectory() && !inTemp.isHidden())
        {
         tempChild.add(new DefaultMutableTreeNode("없음"));
         break;
        }
       }
      }
      parent.add(tempChild);
     }
    }
    parent.remove(0);
   }
   catch(Exception ex)
   {
    JOptionPane.showMessageDialog(frame, "디스크 혹은 파일을 찾을수 없습니다.");
   }
  }
 }
 
 public void valueChanged(TreeSelectionEvent e)
 {
  if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("내컴퓨터")){
   pathText.setText("내컴퓨터");
  }
  else
  {
   try
   {
    pathText.setText(getPath(e));
    pRight=new FView(getPath(e)).getTablePanel();
    pMain.setRightComponent(pRight);
   }
   catch(Exception ex)
   {
    JOptionPane.showMessageDialog(frame, "디스크 혹은 파일을 찾을수 없습니다.");
   }
  }
 }
 
 

 
@Override
public void mouseDragged(MouseEvent e) {
	la.setText("MouseDragged("+e.getX()+","+e.getY()+")");
	
}

@Override
public void mouseMoved(MouseEvent e) {
	la.setText("MouseDragged("+e.getX()+","+e.getY()+")");
	
}

@Override
public void mouseClicked(MouseEvent e) {
	if(e.getClickCount() ==1) {
		int r = (int)(Math.random()*256);
		int g = (int)(Math.random()*256);
		int b = (int)(Math.random()*256);
		
		JPanel p = (JPanel)e.getSource();
		p.setBackground(new Color(r,b,g));
	}
	
}

@Override
public void mouseEntered(MouseEvent e) {
	JPanel p = (JPanel)e.getSource();
	p.setBackground(Color.CYAN);
	
}

@Override
public void mouseExited(MouseEvent e) {
	JPanel p = (JPanel)e.getSource();
	p.setBackground(Color.YELLOW);
	
}

@Override
public void mousePressed(MouseEvent e) {
	la.setText("MousePressed("+e.getX()+","+e.getY()+")");
	
}

@Override
public void mouseReleased(MouseEvent e) {
	la.setText("MouseReleased("+e.getX()+","+e.getY()+")");
	
}
}

class FView
{ 
 private ATable at=new ATable();
 private JTable jt=new JTable(at);
 private JPanel pMain=new JPanel(new BorderLayout());
 private JScrollPane pCenter=new JScrollPane(jt);
 private File file;
 private File list[];
 private long size=0,time=0;

 FView(String str){
  init();
  start(str);
 }

 void init(){
  pMain.add(pCenter,"Center");
 }

 void start(String strPath)
 {
  file=new File(strPath);
  list=file.listFiles();
  at.setValueArr(list.length);
  for(int i=0;i<list.length;++i)
  {
   size=list[i].length();
   time=list[i].lastModified();
   for(int j=0;j<3;++j)
   {
    switch(j)
    {
     case 0:
      at.setValueAt(list[i].getName(),i,j);
      break;
     case 1:
      if(list[i].isFile())
       at.setValueAt(Long.toString((long)Math.round(size/1024.0))+"Kb",i,j);
      break;
     case 2:
      at.setValueAt(getFormatString(time),i,j);
      break;
    }
   }
  }
  jt.repaint();
  pCenter.setVisible(false);
  pCenter.setVisible(true);
 }

 String getLastName(String name)
 {
  int pos=name.lastIndexOf(".");
  String result=name.substring(pos+1,name.length());
  return result;
 }
 
 String getFormatString(long time)
 {
  SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss");
  Date d=new Date(time);
  String temp = sdf.format(d);
  return temp;
 }
 
 JPanel getTablePanel()
 {
  return pMain;
 }
 
}

class ATable extends AbstractTableModel
{
 private String title[]={"name", "size", "modified"};
 private String val[][]=new String[1][3];
 
 public void setValueArr(int i)
 {
  val=new String[i][3];
 }
 
 public int getRowCount()
 {
  return val.length;
 }
 
 public int getColumnCount()
 {
  return val[0].length;
 }
 
 public String getColumnName(int column )
 {
  return title[column];
 }
 
 public boolean isCellEditable(int rowIndex, int columnIndex)
 {
  if(columnIndex==0)
   return true;
  else
   return false;
 }
 
 public Object getValueAt(int row, int column)
 {
  return val[row][column];
 }
 
 public void setValueAt(String aValue, int rowIndex, int columnIndex ){
  val[rowIndex][columnIndex] = aValue;
 }
}