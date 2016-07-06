package com.gameassist.plugin.mc;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gameassist.plugin.adapter.GridViewItemAdapter;
import com.gameassist.plugin.entity.ItemStack;
import com.gameassist.plugin.nativeutils.NativeUtils;


public class PackView extends FrameLayout implements OnClickListener,OnSeekBarChangeListener{

	private GridView mGirdViewItem;

	private  List<ItemStack> mList0;
	private List<ItemStack> mListNewItems = new ArrayList<ItemStack>();
	private List<ItemStack> mListBlock = new ArrayList<ItemStack>();
	private List<ItemStack> mListStone = new ArrayList<ItemStack>();
	private List<ItemStack> mListTool = new ArrayList<ItemStack>();
	private List<ItemStack> mListFood = new ArrayList<ItemStack>();
	private List<ItemStack> mListTree = new ArrayList<ItemStack>();
//	private List<ItemStack> mListColor = new ArrayList<ItemStack>();
	private List<ItemStack> mListEgg = new ArrayList<ItemStack>();
	private List<ItemStack> mListOther = new ArrayList<ItemStack>();
	private List<ItemStack> mListMedicine = new ArrayList<ItemStack>();

	private GridViewItemAdapter mGridViewItemAdapter;
	private SeekBar mSeekBarItemCount;
	private TextView mTextViewItemCount;
	private int mItemCount = 1;

	public PackView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
		initView();
	}

	public PackView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
		initView();
	}
	
	public PackView(Context context) {
		super(context);
		initData();
		initView();
	}

	private void initView() {
		
		inflate(getContext(),R.layout.floor_mainview_item,this);
		findViewById(R.id.floatBtnItemTypeNewItems).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeBlock).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeStone).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeTool).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeFood).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeTree).setOnClickListener(this);
//		findViewById(R.id.floatBtnItemTypeColor).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeEgg).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeOther).setOnClickListener(this);
		findViewById(R.id.floatBtnItemTypeMedicine).setOnClickListener(this);
		
		findViewById(R.id.floatBtnAddItem).setOnClickListener(this);
		
		mSeekBarItemCount = (SeekBar) findViewById(R.id.floatSeekbarItemCount);
		mSeekBarItemCount.setOnSeekBarChangeListener(this);
		mTextViewItemCount = (TextView) findViewById(R.id.floatSeekTextItemCount);

		mGirdViewItem = (GridView)findViewById(R.id.floatGridItemList);
		mGridViewItemAdapter = new GridViewItemAdapter(getContext());
		mGirdViewItem.setAdapter(mGridViewItemAdapter);
		mGridViewItemAdapter.addDatas(mListNewItems);
		
	}


	private void initData() {
		ListItemNew();
		ListItemBlock();
		ListItemStone();
		ListItemTool();
		ListItemFood();
		ListItemTree();
		ListItemEgg();
//		ListItemColor();
		ListItemOther();
		ListItemMedicine();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.floatBtnItemTypeNewItems:
				mGridViewItemAdapter.addDatas(mListNewItems);
				break;
			case R.id.floatBtnItemTypeBlock:
				mGridViewItemAdapter.addDatas(mListBlock);
				break;
			case R.id.floatBtnItemTypeStone:
				mGridViewItemAdapter.addDatas(mListStone);
				break;
			case R.id.floatBtnItemTypeTool:
				mGridViewItemAdapter.addDatas(mListTool);
				break;
			case R.id.floatBtnItemTypeFood:
				mGridViewItemAdapter.addDatas(mListFood);
				break;
			case R.id.floatBtnItemTypeTree:
				mGridViewItemAdapter.addDatas(mListTree);
				break;
//			case R.id.floatBtnItemTypeColor:
//				mGridViewItemAdapter.addDatas(mListColor);
//				break;
			case R.id.floatBtnItemTypeEgg:
				mGridViewItemAdapter.addDatas(mListEgg);
				break;
			case R.id.floatBtnItemTypeOther:
				mGridViewItemAdapter.addDatas(mListOther);
				break;
			case R.id.floatBtnItemTypeMedicine:
				mGridViewItemAdapter.addDatas(mListMedicine);
				break;
//			case R.id.floatButtonSelectAll:
//				mList0=(List<ItemStack>) mGridViewItemAdapter.getCurrentPageItem();
//				Log.d("POSITION", mList0.toString()+"");
//				break;
			case R.id.floatBtnAddItem:
				mList0=(List<ItemStack>) mGridViewItemAdapter.getCurrentPageItem();
				if (mList0.size()!=0) {
					for (int i = 0; i < mList0.size(); i++) {
						if (mList0.get(i).getItemId() > 512) {
							NativeUtils.nativeDoAddItemCheat(1, mList0.get(i).getItemId(), mItemCount,mList0.get(i).getItemDmg());
						}else {
							
							NativeUtils.nativeDoAddItemCheat(1, mList0.get(i).getItemId(), mItemCount,mList0.get(i).getItemDmg());
						}
					}
					Toast.makeText(getContext(), "添加成功!",Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(getContext(), "没有要添加的物品",Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1) {
			progress = 1;
		}
		mItemCount = progress;
		mTextViewItemCount.setText(""+progress);
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	


	private void ListItemNew() {
		mListNewItems.add(new ItemStack(331, 0, false, "红石", 0));
	    mListNewItems.add(new ItemStack(152, 0, true, "红石块", 0));
	    mListNewItems.add(new ItemStack(76, 0, false, "红石火把", 1));
	    mListNewItems.add(new ItemStack(123, 0, false, "红石灯", 1));
	    mListNewItems.add(new ItemStack(143, 5, false, "木质按钮", 5));
	    mListNewItems.add(new ItemStack(77, 5, false, "石质按钮", 5));
	    mListNewItems.add(new ItemStack(72, 0, false, "木质压力板", 1));
	    mListNewItems.add(new ItemStack(70, 0, false, "石质压力板", 1));
	    mListNewItems.add(new ItemStack(126, 0, false, "激活铁轨", 0));
	    mListNewItems.add(new ItemStack(28, 0, false, "探测铁轨", 1));
	    mListNewItems.add(new ItemStack(27, 0, true, "动力铁轨", 0));
	    mListNewItems.add(new ItemStack(25, 0, false, "音符盒", 1));
	    mListNewItems.add(new ItemStack(131, 0, false, "绊线钩", 1));
	    mListNewItems.add(new ItemStack(147, 0, false, "测重压力板 (轻质)", 1));
	    mListNewItems.add(new ItemStack(148, 0, false, "测重压力板 (重质)", 1));
	    mListNewItems.add(new ItemStack(69, 0, false, "拉杆", 1));
	    mListNewItems.add(new ItemStack(151, 0, false, "阳光传感器", 1));
	    mListNewItems.add(new ItemStack(146, 0, false, "陷阱箱", 1));
	    mListNewItems.add(new ItemStack(395, 0, false, "空地图", 1));
	}

//	ListItem.a(ListItem.d, new ItemStack(45, 0, true, "砖块", 2130838572));
//	ListItem.a(ListItem.e, new ItemStack(318, 0, false, "燧石", 2130838406));
//	ListItem.a(ListItem.e, new ItemStack(82, 0, true, "粘土块", 2130838616));
//	ListItem.a(ListItem.d, new ItemStack(139, 0, true, "圆石墙", 2130838197));
//	ListItem.a(ListItem.b, new ItemStack(359, 0, false, "剪刀", 2130838463));
//	ListItem.a(ListItem.d, new ItemStack(139, 1, true, "苔石墙", 2130838198));
//	ListItem.a(ListItem.d, new ItemStack(20, 0, false, "玻璃", 2130838273));

	private void ListItemBlock() {
		   mListBlock.add( new ItemStack(2, 0, true, "草方块",2130837801));
		   mListBlock.add( new ItemStack(3, 0, true, "泥土", 2130837801));
		   mListBlock.add( new ItemStack(1, 0, true, "石头", 2130837801));
		   mListBlock.add( new ItemStack(7, 0, true, "基岩", 2130837801));
		   mListBlock.add( new ItemStack(87, 0, true, "地狱岩", 2130837801));
		   mListBlock.add( new ItemStack(4, 0, true, "圆石", 2130837801));
		   mListBlock.add( new ItemStack(48, 0, true, "苔石", 2130837801));
		   mListBlock.add( new ItemStack(121, 0, true, "末地石", 2130837801));
		   mListBlock.add( new ItemStack(255, 0, true, "光滑石", 2130837801));
		   mListBlock.add( new ItemStack(49, 0, true, "黑曜石", 2130837801));
		   mListBlock.add( new ItemStack(246, 0, true, "黑曜石", 2130837801));
		   mListBlock.add( new ItemStack(98, 0, true, "石砖", 2130837801));
		   mListBlock.add( new ItemStack(98, 1, true, "苔石砖", 2130837801));
		   mListBlock.add( new ItemStack(98, 2, true, "裂石砖", 2130837801));
		   mListBlock.add( new ItemStack(112, 0, true, "地狱砖", 2130837801));
		   mListBlock.add( new ItemStack(155, 0, true, "石英块", 2130837801));
		   mListBlock.add( new ItemStack(155, 1, true, "錾石英", 2130837801));
		   mListBlock.add( new ItemStack(155, 2, true, "柱石英", 2130837801));
		   mListBlock.add( new ItemStack(12, 0, true, "沙子", 2130837801));
		   mListBlock.add( new ItemStack(13, 0, true, "沙砾", 2130837801));
		   mListBlock.add( new ItemStack(24, 0, true, "沙石", 2130837801));
		   mListBlock.add( new ItemStack(24, 1, true, "錾制沙石", 2130837801));
		   mListBlock.add( new ItemStack(24, 2, true, "平滑沙石", 2130837801));
		   mListBlock.add( new ItemStack(5, 0, true, "木板", 2130837801));
		   mListBlock.add( new ItemStack(17, 0, true, "木头", 2130837801));
		   mListBlock.add( new ItemStack(17, 1, true, "松木", 2130837801));
		   mListBlock.add( new ItemStack(17, 2, true, "桦木", 2130837801));
		   mListBlock.add( new ItemStack(18, 0, true, "树叶", 2130837801));
		   mListBlock.add( new ItemStack(18, 1, true, "松树叶", 2130837801));
		   mListBlock.add( new ItemStack(18, 2, true, "桦树叶", 2130837801));
		   mListBlock.add( new ItemStack(60, 0, true, "耕地", 2130837801));
		   mListBlock.add( new ItemStack(110, 0, true, "菌丝砖", 2130837801));
		   mListBlock.add( new ItemStack(243, 0, true, "灰化土", 2130837801));
		   mListBlock.add( new ItemStack(102, 0, true, "玻璃板", 2130837801));
		   mListBlock.add( new ItemStack(170, 0, true, "干草块", 2130837801));
		   mListBlock.add( new ItemStack(95, 0, true, "隐形基岩", 2130837801));
		   mListBlock.add( new ItemStack(52, 0, true, "刷怪笼", 2130837801));
		   mListBlock.add( new ItemStack(172, 0, true, "硬化粘土", 2130837801));
		   mListBlock.add( new ItemStack(47, 0, true, "书架", 2130837801));
		   mListBlock.add( new ItemStack(174, 0, true, "浮冰", 2130837801));
		   mListBlock.add( new ItemStack(30935, 17096, false, "雪", 2130837801, 83));
		   mListBlock.add( new ItemStack(19, 0, true, "海绵", 2130837801));
		   mListBlock.add( new ItemStack(78, 0, false, "雪", 2130837801));
		   mListBlock.add( new ItemStack(44, 0, true, "石台阶", 2130837801));
		   mListBlock.add( new ItemStack(44, 1, true, "沙石台阶", 2130837801));
		   mListBlock.add( new ItemStack(44, 2, true, "木台阶", 2130837801));
		   mListBlock.add( new ItemStack(44, 3, true, "圆台阶", 2130837801));
		   mListBlock.add( new ItemStack(44, 4, true, "砖台阶", 2130837801));
		   mListBlock.add( new ItemStack(44, 5, true, "石砖台阶", 2130837801));
		   mListBlock.add( new ItemStack(44, 6, true, "石英台阶", 2130837801));
		   mListBlock.add( new ItemStack(67, 0, false, "石楼梯", 2130837801));
		   mListBlock.add( new ItemStack(53, 0, false, "木楼梯", 2130837801));
		   mListBlock.add( new ItemStack(108, 0, false, "砖楼梯", 2130837801));
		   mListBlock.add( new ItemStack(109, 0, false, "石砖楼梯", 2130837801));
		   mListBlock.add( new ItemStack(114, 0, false, "地狱梯", 2130837801));
		   mListBlock.add( new ItemStack(128, 0, false, "沙石楼梯", 2130837801));
		   mListBlock.add( new ItemStack(134, 0, false, "云杉梯", 2130837801));
		   mListBlock.add( new ItemStack(135, 0, false, "桦木梯", 2130837801));
		   mListBlock.add( new ItemStack(136, 0, false, "从林梯", 2130837801));
		   mListBlock.add( new ItemStack(156, 0, false, "石英梯", 2130837801));
		   mListBlock.add( new ItemStack(163, 0, false, "双木梯", 2130837801));
		   mListBlock.add( new ItemStack(164, 0, false, "橡木楼梯", 2130838233));
		   mListBlock.add( new ItemStack(159, 0, true, "白色粘土", 2130838215));
		   mListBlock.add( new ItemStack(159, 1, true, "橙色粘土", 2130838216));
		   mListBlock.add( new ItemStack(159, 2, true, "品红粘土", 2130838223));
		   mListBlock.add( new ItemStack(159, 3, true, "淡蓝粘土", 2130838224));
		   mListBlock.add( new ItemStack(159, 4, true, "黄色粘土", 2130838225));
		   mListBlock.add( new ItemStack(159, 5, true, "黄绿粘土", 2130838226));
		   mListBlock.add( new ItemStack(159, 6, true, "粉色粘土", 2130838227));
		   mListBlock.add( new ItemStack(159, 7, true, "灰色粘土", 2130838228));
		   mListBlock.add( new ItemStack(159, 8, true, "淡灰粘土", 2130838229));
		   mListBlock.add( new ItemStack(159, 9, true, "青色粘土", 2130838230));
		   mListBlock.add( new ItemStack(159, 10, true, "紫色粘土", 2130838217));
		   mListBlock.add( new ItemStack(159, 11, true, "蓝色粘土", 2130838218));
		   mListBlock.add( new ItemStack(159, 12, true, "棕色粘土", 2130838219));
		   mListBlock.add( new ItemStack(159, 13, true, "绿色粘土", 2130838220));
		   mListBlock.add( new ItemStack(159, 14, true, "红色粘土", 2130838221));
		   mListBlock.add( new ItemStack(159, 15, true, "黑色粘土", 2130838222));
		   mListBlock.add( new ItemStack(35, 0, true, "白羊毛", 2130838437));
		   mListBlock.add( new ItemStack(35, 1, true, "橙羊毛", 2130838464));
		   mListBlock.add( new ItemStack(35, 2, true, "品红羊毛", 2130838471));
		   mListBlock.add( new ItemStack(35, 3, true, "淡蓝羊毛", 2130838472));
		   mListBlock.add( new ItemStack(35, 4, true, "黄绿羊毛", 2130838473));
		   mListBlock.add( new ItemStack(35, 5, true, "淡绿羊", 2130838474));
		   mListBlock.add( new ItemStack(35, 6, true, "粉羊毛", 2130838475));
		   mListBlock.add( new ItemStack(35, 7, true, "灰羊毛", 2130838476));
		   mListBlock.add( new ItemStack(35, 8, true, "淡灰羊毛", 2130838477));
		   mListBlock.add( new ItemStack(35, 9, true, "青羊毛", 2130838478));
		   mListBlock.add( new ItemStack(35, 10, true, "紫羊毛", 2130838465));
		   mListBlock.add( new ItemStack(35, 11, true, "蓝羊毛", 2130838466));
		   mListBlock.add( new ItemStack(35, 12, true, "棕羊毛", 2130838467));
		   mListBlock.add( new ItemStack(35, 13, true, "绿羊毛", 2130838468));
		   mListBlock.add( new ItemStack(35, 14, true, "红羊毛", 2130838469));
		   mListBlock.add( new ItemStack(35, 15, true, "黑羊毛", 2130838470));
		   mListBlock.add( new ItemStack(171, 0, true, "白色毯", 2130838238));
		   mListBlock.add( new ItemStack(171, 1, true, "橙色毯", 2130838240));
		   mListBlock.add( new ItemStack(171, 2, true, "品红毯", 2130838247));
		   mListBlock.add( new ItemStack(171, 3, true, "淡蓝毯", 2130838248));
		   mListBlock.add( new ItemStack(171, 4, true, "黄地毯", 2130838249));
		   mListBlock.add( new ItemStack(171, 5, true, "黄绿毯", 2130838250));
		   mListBlock.add( new ItemStack(171, 6, true, "粉红毯", 2130838251));
		   mListBlock.add( new ItemStack(171, 7, true, "灰色毯", 2130838252));
		   mListBlock.add( new ItemStack(171, 8, true, "淡灰毯", 2130838253));
		   mListBlock.add( new ItemStack(171, 9, true, "青色毯", 2130838254));
		   mListBlock.add( new ItemStack(171, 10, true, "紫色毯", 2130838241));
		   mListBlock.add( new ItemStack(171, 11, true, "蓝色毯", 2130838242));
		   mListBlock.add( new ItemStack(171, 12, true, "棕色毯", 2130838243));
		   mListBlock.add( new ItemStack(171, 13, true, "绿色毯", 2130838244));
		   mListBlock.add( new ItemStack(171, 14, true, "红色毯", 2130838245));
		   mListBlock.add( new ItemStack(171, 15, true, "黑色毯", 2130838246));
	}
	private void ListItemStone(){
		 mListStone.add(new ItemStack(14, 0, true, "金矿石", 2130838199));
	     mListStone.add(new ItemStack(41, 0, true, "金块", 2130838547));
	     mListStone.add(new ItemStack(266, 0, false, "金锭", 2130838299));
	     mListStone.add(new ItemStack(56, 0, true, "钻石矿", 2130838586));
	     mListStone.add(new ItemStack(57, 0, true, "钻石块", 2130838587));
	     mListStone.add(new ItemStack(264, 0, false, "钻石", 2130838297));
	     mListStone.add(new ItemStack(15, 0, true, "铁矿石", 2130838207));
	     mListStone.add(new ItemStack(42, 0, true, "铁块", 2130838553));
	     mListStone.add(new ItemStack(265, 0, false, "铁锭", 2130838298));
	     mListStone.add(new ItemStack(16, 0, true, "煤矿石", 2130838231));
	     mListStone.add(new ItemStack(173, 0, true, "煤炭块", 2130838256));
	     mListStone.add(new ItemStack(263, 0, false, "煤炭", 2130838295));
	     mListStone.add(new ItemStack(21, 0, true, "青金石矿", 2130838274));
	     mListStone.add(new ItemStack(22, 0, true, "青金石块", 2130838275));
	     mListStone.add(new ItemStack(73, 0, true, "红石矿", 2130838607));
	     mListStone.add(new ItemStack(74, 0, true, "发光红石", 2130838608));
	     mListStone.add(new ItemStack(152, 0, true, "红石块", 2130838209));
	     mListStone.add(new ItemStack(331, 0, false, "红石", 2130838420));
	     mListStone.add(new ItemStack(129, 0, true, "绿宝石矿", 2130838190));
	     mListStone.add(new ItemStack(133, 0, true, "绿宝石", 2130838193));
	     mListStone.add(new ItemStack(89, 0, true, "萤石", 2130838622));
	     mListStone.add(new ItemStack(348, 0, false, "萤石粉", 2130838435));
	}

	private void ListItemTool() {
		mListTool.add( new ItemStack(54, 0, false, "箱子", 2130838585));
		mListTool.add( new ItemStack(58, 0, false, "工作台", 2130838588));
		mListTool.add( new ItemStack(61, 0, false, "熔炉", 2130838592));
		mListTool.add( new ItemStack(245, 0, false, "切石机", 2130838280));
		mListTool.add( new ItemStack(261, 0, false, "弓", 2130838293));
	    mListTool.add(new ItemStack(262, 0, false, "箭", 2130838294));
	    mListTool.add(new ItemStack(259, 0, false, "打火石", 2130838289));
	    mListTool.add(new ItemStack(268, 0, false, "木剑", 2130838301));
	    mListTool.add(new ItemStack(272, 0, false, "石剑", 2130838306));
	    mListTool.add(new ItemStack(267, 0, false, "铁剑", 2130838300));
	    mListTool.add(new ItemStack(283, 0, false, "金剑", 2130838318));
	    mListTool.add(new ItemStack(276, 0, false, "钻石剑", 2130838310));
	    mListTool.add(new ItemStack(269, 0, false, "木锹", 2130838302));
	    mListTool.add(new ItemStack(270, 0, false, "木镐", 2130838304));
	    mListTool.add(new ItemStack(271, 0, false, "木斧", 2130838305));
	    mListTool.add(new ItemStack(290, 0, false, "木锄", 2130838326));
	    mListTool.add(new ItemStack(273, 0, false, "石锹", 2130838307));
	    mListTool.add(new ItemStack(274, 0, false, "石镐", 2130838308));
	    mListTool.add(new ItemStack(275, 0, false, "石斧", 2130838309));
	    mListTool.add(new ItemStack(291, 0, false, "石锄", 2130838327));
	    mListTool.add(new ItemStack(256, 0, false, "铁锹", 2130838286));
	    mListTool.add(new ItemStack(257, 0, false, "铁镐", 2130838287));
	    mListTool.add(new ItemStack(258, 0, false, "铁斧", 2130838288));
	    mListTool.add(new ItemStack(292, 0, false, "铁锄", 2130838328));
	    mListTool.add(new ItemStack(284, 0, false, "金锹", 2130838319));
	    mListTool.add(new ItemStack(285, 0, false, "金镐", 2130838320));
	    mListTool.add(new ItemStack(286, 0, false, "金斧", 2130838321));
	    mListTool.add(new ItemStack(294, 0, false, "金锄", 2130838330));
	    mListTool.add(new ItemStack(277, 0, false, "钻石锹", 2130838311));
	    mListTool.add(new ItemStack(278, 0, false, "钻石镐", 2130838312));
	    mListTool.add(new ItemStack(279, 0, false, "钻石斧", 2130838313));
	    mListTool.add(new ItemStack(293, 0, false, "钻石锄", 2130838329));
	    mListTool.add(new ItemStack(298, 0, false, "皮革帽", 2130838334));
	    mListTool.add(new ItemStack(299, 0, false, "皮外套", 2130838335));
	    mListTool.add(new ItemStack(300, 0, false, "皮裤子", 2130838338));
	    mListTool.add(new ItemStack(301, 0, false, "皮鞋子", 2130838339));
	    mListTool.add(new ItemStack(302, 0, false, "铁链帽", 2130838340));
	    mListTool.add(new ItemStack(303, 0, false, "铁链甲", 2130838341));
	    mListTool.add(new ItemStack(304, 0, false, "铁链腿", 2130838342));
	    mListTool.add(new ItemStack(305, 0, false, "铁链鞋", 2130838343));
	    mListTool.add(new ItemStack(306, 0, false, "铁头盔", 2130838344));
	    mListTool.add(new ItemStack(307, 0, false, "铁胸甲", 2130838345));
	    mListTool.add(new ItemStack(308, 0, false, "铁护腿", 2130838346));
	    mListTool.add(new ItemStack(309, 0, false, "铁鞋子", 2130838349));
	    mListTool.add(new ItemStack(314, 0, false, "金头盔", 2130838402));
	    mListTool.add(new ItemStack(315, 0, false, "金胸甲", 2130838403));
	    mListTool.add(new ItemStack(316, 0, false, "金护腿", 2130838404));
	    mListTool.add(new ItemStack(317, 0, false, "金鞋子", 2130838405));
	    mListTool.add(new ItemStack(310, 0, false, "钻石盔", 2130838358));
	    mListTool.add(new ItemStack(311, 0, false, "钻石甲", 2130838380));
	    mListTool.add(new ItemStack(312, 0, false, "钻石腿", 2130838393));
	    mListTool.add(new ItemStack(313, 0, false, "钻石鞋", 2130838401));
	}
	
	/*
	 *食物
	 */
	private void ListItemFood() {
		// TODO Auto-generated method stub
	      mListFood.add( new ItemStack(86, 0, false, "南瓜", 2130838619));
	      mListFood.add( new ItemStack(92, 0, false, "蛋糕", 2130838626));
	      mListFood.add( new ItemStack(141, 0, false, "胡萝卜", 2130838200));
	      mListFood.add( new ItemStack(142, 0, false, "马铃薯", 2130838201));
	      mListFood.add( new ItemStack(244, 0, false, "甜菜根", 2130838279));
	      mListFood.add( new ItemStack(260, 0, false, "红苹果", 2130838292));
	      mListFood.add( new ItemStack(282, 0, false, "蘑菇汤", 2130838317));
	      mListFood.add( new ItemStack(297, 0, false, "面包", 2130838333));
	      mListFood.add( new ItemStack(319, 0, false, "生猪排", 2130838407));
	      mListFood.add( new ItemStack(320, 0, false, "热猪排", 2130838408));
	      mListFood.add( new ItemStack(338, 0, false, "甘蔗", 2130838426));
	      mListFood.add( new ItemStack(353, 0, false, "糖", 2130838457));
	      mListFood.add( new ItemStack(354, 0, false, "蛋糕", 2130838458));
	      mListFood.add( new ItemStack(357, 0, false, "饼干", 2130838461));
	      mListFood.add( new ItemStack(360, 0, false, "西瓜片", 2130838479));
	      mListFood.add( new ItemStack(363, 0, false, "生牛肉", 2130838482));
	      mListFood.add( new ItemStack(364, 0, false, "牛排", 2130838483));
	      mListFood.add( new ItemStack(365, 0, false, "生鸡肉", 2130838484));
	      mListFood.add( new ItemStack(366, 0, false, "熟鸡肉", 2130838485));
	      mListFood.add( new ItemStack(391, 0, false, "胡萝卜", 2130838528));
	      mListFood.add( new ItemStack(392, 0, false, "土豆", 2130838529));
	      mListFood.add( new ItemStack(393, 0, false, "烤土豆", 2130838530));
	      mListFood.add( new ItemStack(400, 0, false, "南瓜饼", 2130838541));
	      mListFood.add( new ItemStack(457, 0, false, "甜菜根", 2130838573));
	      mListFood.add( new ItemStack(459, 0, false, "甜菜汤", 2130838575));
	      mListFood.add( new ItemStack(349, 0, false, "生鱼", 2130838436));
	      mListFood.add( new ItemStack(350, 0, false, "熟鱼", 2130838438));
	      mListFood.add( new ItemStack(367, 0, false, "腐肉", 2130838486));
	}
	
	private void ListItemTree() {
		   	 mListTree.add(new ItemStack(6, 0, false, "橡树苗", 2130838590));
			 mListTree.add(new ItemStack(6, 1, false, "云杉苗", 2130838601));
			 mListTree.add(new ItemStack(6, 2, false, "桦树苗", 2130838602));
			 mListTree.add(new ItemStack(37, 0, false, "蒲公英", 2130838488));
			 mListTree.add(new ItemStack(38, 0, false, "青色花", 2130838499));
			 mListTree.add(new ItemStack(39, 0, false, "棕蘑菇", 2130838527));
			 mListTree.add(new ItemStack(40, 0, false, "红蘑菇", 2130838540));
			 mListTree.add(new ItemStack(81, 0, false, "仙人掌", 2130838615));
			 mListTree.add(new ItemStack(83, 0, false, "甘蔗", 2130838617));
			 mListTree.add(new ItemStack(103, 0, false, "西瓜", 2130838146));
			 mListTree.add(new ItemStack(104, 0, false, "南瓜梗", 2130838170));
			 mListTree.add(new ItemStack(105, 0, false, "西瓜梗", 2130838171));
			 mListTree.add(new ItemStack(106, 0, false, "葡萄树", 2130838172));
			 mListTree.add(new ItemStack(111, 0, false, "睡莲", 2130838178));
			 mListTree.add(new ItemStack(175, 0, false, "向日葵", 2130838258));
			 mListTree.add(new ItemStack(175, 1, false, "丁香花", 2130838260));
			 mListTree.add(new ItemStack(175, 2, false, "高杆草", 2130838261));
			 mListTree.add(new ItemStack(175, 3, false, "蕨类", 2130838262));
			 mListTree.add(new ItemStack(175, 4, false, "蔷薇", 2130838263));
			 mListTree.add(new ItemStack(175, 5, false, "牡丹", 2130838264));
			 mListTree.add(new ItemStack(295, 0, false, "小麦种", 2130838331));
			 mListTree.add(new ItemStack(296, 0, false, "小麦", 2130838332));
			 mListTree.add(new ItemStack(361, 0, false, "南瓜子", 2130838480));
			 mListTree.add(new ItemStack(362, 0, false, "西瓜子", 2130838481));
			 mListTree.add(new ItemStack(458, 0, false, "甜菜子", 2130838574));
	}
	
//	ListItem.a(ListItem.h, new ItemStack(31039, 17026, false, "闪烁的西瓜", 2130838370, 13));
//	ListItem.a(ListItem.h, new ItemStack(31092, 17015, false, "金苹果", 2130838378, 2));
//	ListItem.a(ListItem.h, new ItemStack(31181, 17044, false, "兔子腿", 2130838387, 31));
//	ListItem.a(ListItem.h, new ItemStack(31102, 17020, false, "金苹果", 2130838381, 8));
//	ListItem.a(ListItem.h, new ItemStack(263, 1, false, "木炭", 2130838296));
//	ListItem.a(ListItem.h, new ItemStack(30978, 17043, false, "木船", 2130838352, 27));
//	ListItem.a(ListItem.h, new ItemStack(30987, 17027, false, "相思船", 2130838354, 18));
//	ListItem.a(ListItem.h, new ItemStack(30986, 17037, false, "黑橡木船", 2130838353, 19));
//	ListItem.a(ListItem.h, new ItemStack(31064, 17022, false, "曲奇", 2130838374, 9));
//	ListItem.a(ListItem.h, new ItemStack(31142, 17015, false, "南瓜派", 2130838384, 2));
//	ListItem.a(ListItem.h, new ItemStack(31055, 17014, false, "岩浆膏", 2130838373, 1));
//	ListItem.a(ListItem.h, new ItemStack(30888, 17010, false, "严重损坏的铁砧", 2130838348, 5));

	/*
	 * 药水
	 */
	
	private void ListItemMedicine() {
		  mListMedicine.add(new ItemStack(384, 0, false, "附魔之瓶", 1));
	      mListMedicine.add(new ItemStack(374, 0, false, "玻璃瓶", 1));
	      mListMedicine.add(new ItemStack(373, 0, false, "水瓶", 1));
	      mListMedicine.add(new ItemStack(373, 1, false, "平凡药水", 1));
	      mListMedicine.add(new ItemStack(373, 2, false, "平凡药水", 1));
	      mListMedicine.add(new ItemStack(373, 3, false, "浓稠药水", 1));
	      mListMedicine.add(new ItemStack(373, 4, false, "粗制药水", 1));
	      mListMedicine.add(new ItemStack(373, 5, false, "夜视(3)", 1));
	      mListMedicine.add(new ItemStack(373, 6, false, "夜视(8)", 1));
	      mListMedicine.add(new ItemStack(373, 7, false, "隐身(3)", 1, 41));
	      mListMedicine.add(new ItemStack(31035, 17031, false, "隐身(8)", 11, 26));
	      mListMedicine.add(new ItemStack(31029, 17032, false, "跳跃(3)", 1, 12));
	      mListMedicine.add(new ItemStack(31009, 17055, false, "跳跃(8)", 11, 32));
	      mListMedicine.add(new ItemStack(31028, 17033, false, "跳跃(II)", 11, 13));
	      mListMedicine.add(new ItemStack(31010, 17044, false, "抗火(3)", 1, 35));
	      mListMedicine.add(new ItemStack(31223, 17102, false, "抗火(8)", 11, 78));
	      mListMedicine.add(new ItemStack(31033, 17027, false, "迅捷(3)", 1, 24));
	      mListMedicine.add(new ItemStack(31027, 17032, false, "迅捷(8)", 11, 18));
	      mListMedicine.add(new ItemStack(31025, 17045, false, "迅捷(II)", 11, 16));
	      mListMedicine.add(new ItemStack(31009, 17028, false, "迟缓(1.30)", 1, 32));
	      mListMedicine.add(new ItemStack(31230, 17118, false, "迟缓(4)", 11, 87));
	      mListMedicine.add(new ItemStack(31029, 17042, false, "水肺(3)", 1, 12));
	      mListMedicine.add(new ItemStack(31026, 17052, false, "水肺(8)", 11, 19));
	      mListMedicine.add(new ItemStack(31005, 17084, false, "治疗(I)", 1, 52));
	      mListMedicine.add(new ItemStack(31029, 17047, false, "治疗(II)", 11, 12));
	      mListMedicine.add(new ItemStack(31038, 17051, false, "伤害(I)", 1, 23));
	      mListMedicine.add(new ItemStack(31043, 17007, false, "伤害(II)", 11, 2));
	      mListMedicine.add(new ItemStack(31053, 16992, false, "剧毒(0.45)", 1, 4));
	      mListMedicine.add(new ItemStack(31054, 16998, false, "剧毒(2)", 11, 7));
	      mListMedicine.add(new ItemStack(31042, 16995, false, "剧毒(II)", 11, 3));
	      mListMedicine.add(new ItemStack(31054, 16992, false, "再生(0.45)", 1, 7));
	      mListMedicine.add(new ItemStack(31042, 16997, false, "再生(2)", 11, 3));
	      mListMedicine.add(new ItemStack(30996, 17084, false, "再生(II)", 11, 45));
	      mListMedicine.add(new ItemStack(31011, 17032, false, "力量(3)", 1, 34));
	      mListMedicine.add(new ItemStack(31029, 17057, false, "力量(8)", 11, 12));
	      mListMedicine.add(new ItemStack(30978, 17049, false, "力量(II)", 11, 67));
	      mListMedicine.add(new ItemStack(30996, 17024, false, "虚弱(1.30)", 1, 45));
	      mListMedicine.add(new ItemStack(31224, 17133, false, "虚弱(4)", 11, 89));
	      mListMedicine.add(new ItemStack(31227, 17038, false, "喷溅型水瓶", 1, 25));
	      mListMedicine.add(new ItemStack(31214, 17048, false, "喷溅型平凡", 11, 36));
	      mListMedicine.add(new ItemStack(31226, 17039, false, "喷溅型平凡", 11, 24));
	      mListMedicine.add(new ItemStack(31223, 17025, false, "喷溅型浓稠", 11, 13));
	      mListMedicine.add(new ItemStack(31200, 17043, false, "喷溅型粗制", 1, 34));
	      mListMedicine.add(new ItemStack(31194, 17064, false, "喷溅型夜视(3)", 1, 56));
	      mListMedicine.add(new ItemStack(31035, 17096, false, "喷溅型夜视(8)", 11, 89));
	      mListMedicine.add(new ItemStack(31194, 17066, false, "喷溅型隐身(3)", 1, 56));
	      mListMedicine.add(new ItemStack(31198, 17057, false, "喷溅型隐身(8)", 11, 52));
	      mListMedicine.add(new ItemStack(31203, 17055, false, "喷溅型跳跃(3)", 1, 33));
	      mListMedicine.add(new ItemStack(31229, 17030, false, "喷溅型跳跃(3)", 11, 23));
	      mListMedicine.add(new ItemStack(31118, 17010, false, "喷溅型跳跃(II)", 11, 4));
	      mListMedicine.add(new ItemStack(31117, 17008, false, "喷溅型抗火(3)", 1, 7));
	      mListMedicine.add(new ItemStack(31202, 17048, false, "喷溅型抗火(8)", 11, 32));
	      mListMedicine.add(new ItemStack(31191, 17068, false, "喷溅型迅捷(3)", 1, 45));
	      mListMedicine.add(new ItemStack(31194, 17058, false, "喷溅型迅捷(8)", 11, 56));
	      mListMedicine.add(new ItemStack(31169, 17064, false, "喷溅型迅捷(II)", 11, 67));
	      mListMedicine.add(new ItemStack(31222, 17040, false, "喷溅型迟缓(1.30)", 1, 12));
	      mListMedicine.add(new ItemStack(31229, 17054, false, "喷溅型迟缓(4)", 11, 23));
	      mListMedicine.add(new ItemStack(31191, 17073, false, "喷溅型水肺(3)", 1, 45));
	      mListMedicine.add(new ItemStack(31194, 17081, false, "喷溅型水肺(8)", 11, 56));
	      mListMedicine.add(new ItemStack(31169, 17069, false, "喷溅型治疗(I)", 1, 67));
	      mListMedicine.add(new ItemStack(31200, 17025, false, "喷溅型治疗(II)", 11, 34));
	      mListMedicine.add(new ItemStack(31115, 17001, false, "喷溅型伤害(I)", 1, 9));
	      mListMedicine.add(new ItemStack(31118, 16993, false, "喷溅型伤害(II)", 11, 4));
	      mListMedicine.add(new ItemStack(31222, 17048, false, "喷溅型剧毒(0.45)", 1, 12));
	      mListMedicine.add(new ItemStack(31222, 17051, false, "喷溅型剧毒(2)", 11, 12));
	      mListMedicine.add(new ItemStack(31229, 17047, false, "喷溅型剧毒(II)", 11, 23));
	      mListMedicine.add(new ItemStack(31222, 17053, false, "喷溅型再生(0.45)", 1, 12));
	      mListMedicine.add(new ItemStack(31200, 17034, false, "喷溅型再生(2)", 11, 34));
	      mListMedicine.add(new ItemStack(31194, 17075, false, "喷溅型再生(II)", 11, 56));
	      mListMedicine.add(new ItemStack(31200, 17032, false, "喷溅型力量(3)", 1, 34));
	      mListMedicine.add(new ItemStack(31229, 17068, false, "喷溅型力量(8)", 11, 23));
	      mListMedicine.add(new ItemStack(31194, 17036, false, "喷溅型力量(II)", 11, 56));
	      mListMedicine.add(new ItemStack(31227, 17068, false, "喷溅型虚弱(1.30)", 1, 25));
	      mListMedicine.add(new ItemStack(31202, 17078, false, "喷溅型虚弱(4)", 11, 32));
	}
	
	
	
	
	private void ListItemEgg() {
		// TODO Auto-generated method stub
	        mListEgg.add( new ItemStack(383, 45, false, "女巫蛋", 2130838524));
	        mListEgg.add( new ItemStack(383, 18, false, "兔子", 2130838509));
	        mListEgg.add( new ItemStack(383, 22, false, "豹猫", 2130838511));
	        mListEgg.add( new ItemStack(383, 43, false, "火焰幼体", 2130838523));
	        mListEgg.add( new ItemStack(383, 10, false, "鸡", 2130838501));
	        mListEgg.add( new ItemStack(383, 11, false, "牛", 2130838502));
	        mListEgg.add( new ItemStack(383, 12, false, "猪", 2130838503));
	        mListEgg.add( new ItemStack(383, 13, false, "羊", 2130838504));
	        mListEgg.add( new ItemStack(383, 14, false, "狼", 2130838505));
	        mListEgg.add( new ItemStack(383, 15, false, "村民", 2130838506));
	        mListEgg.add( new ItemStack(383, 16, false, "哞菇", 2130838507));
	        mListEgg.add( new ItemStack(383, 17, false, "鱿鱼", 2130838508));
	        mListEgg.add( new ItemStack(383, 19, false, "蝙蝠", 2130838510));
	        mListEgg.add( new ItemStack(383, 20, false, "铁傀儡", 2130838501));
	        mListEgg.add( new ItemStack(383, 21, false, "雪傀儡", 2130838501));
	        mListEgg.add( new ItemStack(383, 32, false, "僵尸", 2130838512));
	        mListEgg.add( new ItemStack(383, 33, false, "爬行者", 2130838513));
	        mListEgg.add( new ItemStack(383, 34, false, "骷髅", 2130838514));
	        mListEgg.add( new ItemStack(383, 35, false, "蜘蛛", 2130838515));
	        mListEgg.add( new ItemStack(383, 36, false, "僵尸猪人", 2130838516));
	        mListEgg.add( new ItemStack(383, 37, false, "史莱姆", 2130838517));
	        mListEgg.add( new ItemStack(383, 38, false, "末影人", 2130838518));
	        mListEgg.add( new ItemStack(383, 39, false, "蠹虫", 2130838519));
	        mListEgg.add( new ItemStack(383, 40, false, "洞穴蜘蛛", 2130838520));
	        mListEgg.add( new ItemStack(383, 41, false, "恶魂", 2130838521));
	        mListEgg.add( new ItemStack(383, 42, false, "岩浆怪", 2130838522));
		
	}
	

	private void ListItemOther() {
		mListOther.add(new ItemStack(8, 0, true, "水", 2130838613));
	    mListOther.add(new ItemStack(9, 0, true, "静止的水", 2130838623));
	    mListOther.add(new ItemStack(10, 0, true, "岩浆", 2130838143));
	    mListOther.add(new ItemStack(11, 0, true, "静止的浆", 2130838176));
	    mListOther.add(new ItemStack(336, 0, false, "砖头", 2130838424));
	    mListOther.add(new ItemStack(337, 0, false, "粘土", 2130838425));
	    mListOther.add(new ItemStack(66, 0, true, "铁轨", 2130838597));
	    mListOther.add(new ItemStack(26, 0, false, "残缺床", 2130838291));
	    mListOther.add(new ItemStack(355, 0, false, "床", 2130838459));
	    mListOther.add(new ItemStack(63, 0, false, "残缺木牌", 2130838594));
	    mListOther.add(new ItemStack(68, 0, false, "墙告示", 2130838599));
	    mListOther.add(new ItemStack(323, 0, false, "告示牌", 2130838410));
	    mListOther.add(new ItemStack(64, 0, false, "残缺木门", 2130838595));
	    mListOther.add(new ItemStack(324, 0, false, "木门", 2130838411));
	    mListOther.add(new ItemStack(107, 0, true, "栅栏门", 2130838173));
	    mListOther.add(new ItemStack(183, 0, true, "棕栅栏门", 2130838268));
	    mListOther.add(new ItemStack(96, 0, false, "活板门", 2130838628));
	    mListOther.add(new ItemStack(325, 0, false, "桶", 2130838412));
	    mListOther.add(new ItemStack(325, 1, false, "牛奶桶", 2130838414));
	    mListOther.add(new ItemStack(325, 8, false, "水桶", 2130838415));
	    mListOther.add(new ItemStack(325, 10, false, "岩浆桶", 2130838416));
	    mListOther.add(new ItemStack(50, 0, false, "火把", 2130838581));
	    mListOther.add(new ItemStack(51, 0, false, "火", 2130838582));
	    mListOther.add(new ItemStack(91, 0, false, "南瓜灯", 2130838625));
	    mListOther.add(new ItemStack(85, 0, false, "栅栏", 2130838618));
	    mListOther.add(new ItemStack(101, 0, false, "铁栏杆", 2130838144));
	    mListOther.add(new ItemStack(120, 0, true, "末地门", 2130838184));
	    mListOther.add(new ItemStack(247, 0, true, "反应核", 2130838282));
	    mListOther.add(new ItemStack(46, 0, true, "TNT", 2130838576));
	    mListOther.add(new ItemStack(405, 0, false, "地狱砖", 2130838543));
	    mListOther.add(new ItemStack(65, 0, false, "梯子", 2130838596));
	    mListOther.add(new ItemStack(31, 0, false, "枯灌木", 2130838357));
	    mListOther.add(new ItemStack(30, 0, true, "蜘蛛网", 2130838337));
	    mListOther.add(new ItemStack(280, 0, false, "木棍", 2130838315));
	    mListOther.add(new ItemStack(281, 0, false, "碗", 2130838316));
	    mListOther.add(new ItemStack(287, 0, false, "线", 2130838322));
	    mListOther.add(new ItemStack(288, 0, false, "羽毛", 2130838323));
	    mListOther.add(new ItemStack(289, 0, false, "火药", 2130838324));
	    mListOther.add(new ItemStack(321, 0, false, "画", 2130838409));
	    mListOther.add(new ItemStack(328, 0, false, "矿车", 2130838417));
	    mListOther.add(new ItemStack(333, 0, false, "小船", 2130838422));
	    mListOther.add(new ItemStack(346, 0, false, "钓鱼竿", 2130838433));
	    mListOther.add(new ItemStack(329, 0, false, "鞍", 2130838418));
	    mListOther.add(new ItemStack(332, 0, false, "雪球", 2130838421));
	    mListOther.add(new ItemStack(334, 0, false, "皮革", 2130838423));
	    mListOther.add(new ItemStack(339, 0, false, "纸", 2130838427));
	    mListOther.add(new ItemStack(340, 0, false, "书本", 2130838428));
	    mListOther.add(new ItemStack(341, 0, false, "粘液球", 2130838429));
	    mListOther.add(new ItemStack(344, 0, false, "鸡蛋", 2130838431));
	    mListOther.add(new ItemStack(345, 0, false, "指南针", 2130838432));
	    mListOther.add(new ItemStack(347, 0, false, "时钟", 2130838434));
	    mListOther.add(new ItemStack(352, 0, false, "骨头", 2130838456));
	}

	
}
