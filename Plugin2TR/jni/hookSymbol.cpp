#include "hookutils.h"
using namespace std;

int lockCurrTime=0;
int changeToDay=0;
int changeToNight=0;
int isRaining=-1;
int noRain=0;
int haveLightning=-1;
int startToBloodMoon=-1;
int isPumpkinMoon=-1;
int isStartEclipse=-1;
int isStartFrostMoon=-1;
int wingType=-1;
int lockPlayerHP=0;
int lockPlayerMP=0;
int playerCurrMaxHP=0;
int lockNotAPlayer=0;
int lockSArea=0;
int isKillSelf=0;
int lockFlyTimeMax=0;
int festiveOder=0;
int IsAllowedGoblin=0;
int IsAllowedPirate=0;
int IsAllowedSnowman=0;
int IsAllowedMartianMan=0;
int IsAllowedMoonMan=0;
int timeSpeed=0;
int gameTimeValue=0;
/**
 * 物品添加控制
 * */
int baseA=0;
int IsInGameing=0;

int (*old_KillMe)(int a1,float a2,int a3,int a4,int a5) = NULL;
int (*old_GetPlayer)() = NULL;
int (*old_itemItem)(int a1) = NULL;
int (*old_itemInit)(int a1) = NULL;
int (*old_timeReset)(int a1,int a2) = NULL;
int (*old_updateNight)(int a1) = NULL;
int (*old_ChangeRain)(int a1) = NULL;
int (*old_StartRain)(int a1) = NULL;
int (*old_UpdateClouds)(int a1,int a2,int a3) = NULL;
int (*old_UpdateLightning)(int a1) = NULL;
int (*old_startBloodMoon)(int a1) = NULL;
int (*old_sunMoonTransition)(int a1,int a2) = NULL;
int (*old_SpawnHardModeBoss)(int a1) = NULL;
int (*old_startPumpkinMoon)(int a1) = NULL;
int (*old_stopPumpkinMoon)(int a1) = NULL;
int (*old_startEclipse)(int a1) = NULL;
int (*old_startFrostMoon)(int a1) = NULL;
int (*old_stopFrostMoon)(int a1) = NULL;
int (*old_StartInvasion)(int a1,int a2,int a3,int a4) = NULL;
int (*old_GetMainPlayer)() = NULL;
int (*old_floatsisf)(int a1) = NULL;
int (*old_itemGetType)(int a1) = NULL;
int (*old_itemsetType)(int a1,int a2) = NULL;
int (*old_AddItemToCraftingLists)(int a1,int a2) = NULL;
int (*old_CopyItem)(int src,int a2) = NULL;
int (*old_NewItem)(int Item, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9,int a10) = NULL;
int (*old_SetStack)(int a1,int a2 ) = NULL;
int (*old_GetStack)(int a1 ) = NULL;
int (*old_SetDefaults)(int item, int type,int count,int a4) = NULL;  //设置默认值
int (*old_CountEmptySlots)(int a1 ) = NULL;//获取剩余插槽数量
int (*old_ClearInventory)(int player ) = NULL;
int (*old_FindItem)(int player, int type) = NULL;  //从仓库中查找有没有指定类型的物品。返回对应仓库id









//int (*old_IsGoblinInvasionAllowed)(int a1) = NULL;

/*
 * 327   黄金
329  暗影

1141  神庙
1533  丛林
1534  腐化
1535  血腥
1536  神圣
1537  冰霜*/
int newItemArray = operator new(0xAC);   //_ZN9Inventory9SwapItemsEP4ItemS1_b
int newitem(){
 old_itemItem(newItemArray);
 return old_itemInit(newItemArray);
}
int findEmptySlotID(){ //返回仓库空插槽id
	for(int i=0;i<48;i++){
		 int itemPoint=(char *)old_GetPlayer() + 172 * i + 4972;
		 int itemCount=old_GetStack(itemPoint);
		 if(itemCount<1){
			 return i;
		 }
	}
	return -1;
}
int  addItem(int itemType,int itemCount){  //48个槽 0---47
	int player=old_GetPlayer();
int slotID=	old_FindItem(player,itemType); //返回id
if(slotID>-1){  //找到对应物品只需设置数量即可
	int itemPoint=(char *)old_GetPlayer() + 172 * slotID + 4972; //获取仓库中对应此id（a2）的ITEM对象
	int srcCount=old_GetStack(itemPoint);
	int conutSum=itemCount+srcCount;
	if(conutSum<1){
		conutSum=0;
	}
	old_SetStack(itemPoint,conutSum);
	return 1;
}else{   // 查找空插槽
int 	emptySoltID=findEmptySlotID();
if(emptySoltID>-1){
if(itemCount<1){
	return itemCount;
}
int item=newitem();
 old_SetDefaults(item,itemType,itemCount,0);
 //int a2=27; //插槽位置//获取可用插槽数量及位置
 int itemPoint=(char *)player + 172 * emptySoltID + 4972; //获取仓库中对应此id（a2）的ITEM对象
 old_CopyItem(item,itemPoint);
 return 1;
}else{
return 0; //仓库已满
}
}
 return 1;
}

void invasionGoblin(int a1){
	*(int *)(baseA+0x65850C)=0;//入侵延时


	 int player=*(int *)(baseA+0x658520); //myplayer
	//LogD("<%s> player=%d bosscount=%d", __FUNCTION__,player,*(int *)(baseA+0x658510));
	 *(int *)(baseA+0x658518)=0;  //入侵类型
	*(int *)(baseA+0x658510)=0;//入侵数量
	 playerCurrMaxHP=*(unsigned char *)(player+0x6D50);
	//LogD("<%s> type=%d", __FUNCTION__,*(int *)(baseA+0x658518));
	*(unsigned char *)(player+0x6D50)=200;//最大血量 初始化值为100
	  int myplayer=old_GetMainPlayer();
	 *(unsigned char *)(myplayer+0x6D77)=28;
	  old_StartInvasion(a1,0,0,0);
	  *(unsigned char *)(player+0x6D50)=playerCurrMaxHP;
	/*  LogD("<%s> type=%d", __FUNCTION__,*(int *)(baseA+0x658518));
	  LogD("<%s> player=%d bosscount=%d", __FUNCTION__,player,*(int *)(baseA+0x658510));
	  LogD("<%s> myplayer=%d", __FUNCTION__,*(unsigned char *)(myplayer+0x6D77));
	  LogD("<%s> case1=%d Hp=%d", __FUNCTION__,*(unsigned char *)(player+0x6D0F),*(unsigned char *)(player+0x6D50));*/


 /**(int *)(baseA+0x658518)=a1; //入侵类型//入侵结束是值等于入侵类型。 1:哥布林，2.寒霜。
	*(int *)(baseA+0x658510)=70;//怪物数量
	int maxTiles=*(int *)(baseA+0x5583E2); //获取坐标值
	int invasionX=old_floatsisf(maxTiles);
	int player1=*(int *)(baseA+0x658520);
	int invasionDelay=*(int *)(baseA+0x65850C);
	 LogD("<%s> invasionDelay=%d", __FUNCTION__,invasionDelay);
	*(unsigned char *)(player1+0x6D50)=200;//最大血量 初始化值为100
	 LogD("<%s> maxTiles=%d invasionX=%d", __FUNCTION__,maxTiles,invasionX);
	*(int *)(baseA+0x658514)=old_floatsisf(maxTiles);*/

}
void doGameEvent(int flag,int arg1,int arg2){ //游戏事件处理
	switch(flag){
	case 1:
		//IsAllowedGoblin = arg2;  //哥布林入侵
		if(arg2==1){
		 invasionGoblin(1);
		}
		 //*(int *)(baseA+0x658518);
		 /* *(int *)(baseA+0x658518)=1;//入侵结束是值等于入侵类型。 1:哥布林，2.寒霜。
		  *(int *)(baseA+0x658510)=120;
		  int maxTiles=*(int *)(baseA+0x5583E2);
		  old_StartInvasion(1,0,0,0);*/
		  //658514
		//  LogD("<%s> invasionX=%d", __FUNCTION__,*(int *)(baseA+0x658514));
		/*int myplayer=old_GetMainPlayer();
		 LogD("<%s> invasionType=%d", __FUNCTION__,*(int *)(old_GetMainPlayer()+27984));*/
		/* *(unsigned char *)(old_GetMainPlayer()+28023)=28;
		 LogD("<%s> invasionType=%d", __FUNCTION__,*(unsigned char *)(old_GetMainPlayer()+28023));
		 LogD("<%s> invasionType=%d", __FUNCTION__,*(int *)(baseA+0x658518));
		old_StartInvasion(1,0,0,0);*/
		/*old_StartInvasion(2,0,0,0);
		old_StartInvasion(3,0,0,0);
		old_StartInvasion(4,0,0,0);*/
		break;
	case 2:
		//IsAllowedPirate = arg2; //海盗入侵
		if(arg2==1){
		invasionGoblin(3);
		}
		break;
	case 3:
	//IsAllowedSnowman= arg2; //雪人入侵
		if (arg2 == 1) {
			invasionGoblin(2);
		}
	break;
	case 4:
		if(arg2==1){
		invasionGoblin(4);
		}
//	IsAllowedMartianMan = arg2; //火星人入侵
	break;
	case 5:
		if(arg2==1){
		invasionGoblin(5);
		}
//	IsAllowedMoonMan = arg2; //火星人入侵
	break;
	default:
	{
		break;
	}
}
}

/*
 * 327   黄金
329  暗影

1141  神庙
1533  丛林
1534  腐化
1535  血腥
1536  神圣
1537  冰霜*/


void doRoleCheat(int flag,int arg1,int arg2){//角色控制
	switch(flag){
	case 1:
		lockPlayerHP = arg2;  //无限生命
		break;
	case 2:
		lockPlayerMP = arg2; //无限蓝
		break;
	case 3:
		lockNotAPlayer = arg2; //对怪物隐身
		break;
	case 4:
		lockSArea = arg2;       //区域范围内怪物伤害
		break;
	case 5:
		isKillSelf = arg2;      //自杀
		break;
	case 6:
		wingType = arg2;      //装备翅膀.
		break;
	case 7:
		lockFlyTimeMax=arg2;
		break;
		   default:{
			   break;
		   }
		}
}


int IsInGame(){
	int ret=0;
	if(IsInGameing){
		ret=1;
	}
	IsInGameing=*(unsigned char *)(baseA+0x69FEC4); //_ZN4Main13isGameStartedE   Main::isGameStarted
	// LogD("<%s> ret=%d", __FUNCTION__,ret);
	return ret;
}

/*
 * UI控制逻辑
 */
int  doAddItemCheat(int flag,int itemId,int itemCount){
	int ret=addItem(itemId,itemCount);
	 return ret;
}
void changeForNight(){ //转换到夜晚.
 	if(gameTimeValue){
 	//LogD("<%s> gametime=%d", __FUNCTION__,gameTimeValue);
	old_timeReset(gameTimeValue, 0.01);
	*(int *) (gameTimeValue + 4) = 1209840512; //转换到夜晚
	}
}
void changToBloodMoon(){
 if(gameTimeValue){
	changeForNight ();
	int r1 = old_startBloodMoon(gameTimeValue);
	old_SpawnHardModeBoss(r1);
	old_sunMoonTransition(old_GetPlayer(), 0);
	}
}
void stopBloodMoon(){
	 changeForNight();
}
void changToPumpkinMoon(){
	if(gameTimeValue){
	changeForNight();
	old_startPumpkinMoon(gameTimeValue);
	}
}
void stopPumpkinMoon(){
	if(gameTimeValue){
	old_stopPumpkinMoon(gameTimeValue);
	changeForNight();
	}
}
void changToStartFrostMoon(){
	if(gameTimeValue){
	changeForNight();
	old_startFrostMoon(gameTimeValue);
	}
}
void stopFrostMoon(){
	if(gameTimeValue){
	old_stopFrostMoon(gameTimeValue);
	changeForNight();
	}
}
void changToStartEclipse(){
	if(gameTimeValue){
	old_timeReset(gameTimeValue, 0.01); //转到白天
	old_startEclipse(gameTimeValue);
	}
}
void stopStartEclipse(){
	if(gameTimeValue){
	old_timeReset(gameTimeValue, 0.01);
	*(unsigned char *) (gameTimeValue + 9) = 0;
	//LogD("<%s> isStartEclipse=%d", __FUNCTION__, isStartEclipse);
	}
}
void doTimeCheat(int flag, int arg1, int arg2) {
// LogD("<%s> flag=%d arg1=%d timeSpeed=%d", __FUNCTION__,flag,arg1,arg2);
	if (flag == 1) {
		festiveOder=arg2;  //节日控制
	}else if(flag==2){
		lockCurrTime = arg2;//锁定时间
		 //LogD("<%s> lockCurrTime%d", __FUNCTION__,lockCurrTime);
	} else if (flag == 3) {
		changeToDay=1;
		//锁定白天
	 //LogD("<%s> changeToDay%d", __FUNCTION__,changeToDay);
	} else if (flag == 4) {
		changeToNight=1;
	///	LogD("<%s> changeToNight%d", __FUNCTION__,changeToNight);
       //锁定夜晚
	} else if (flag == 5) { //永不下雨
		noRain=arg2;
	} else if (flag == 11) {  //开始下雨
		 haveLightning=arg2;
	} else if (flag == 12) {  //打雷  int noRain=0;
		//LogD("<%s> isRaining%d", __FUNCTION__,isRaining);
			isRaining=arg2;
	} else if (flag == 13) {//血月

			if(arg2==1){
				changToBloodMoon();
			}else{
				stopBloodMoon();
			}
	} else if (flag == 14) {//南瓜月
			if(arg2==1){
				changToPumpkinMoon();
			}else{
				stopPumpkinMoon();
			}
	} else if (flag == 15) {   //日食
		if(arg2==1){
			changToStartEclipse();
		}else{
			stopStartEclipse();
		}
	}else if (flag ==16) {//霜月
		if(arg2==1){
			changToStartFrostMoon();
		}else{
			stopFrostMoon();
		}
	}else if (flag == 17) {
		timeSpeed=arg2;
	}
}

static int screenshot = 0;
jclass sshelper = NULL;
jmethodID method1 = NULL;
static JavaVM *gs_jvm = NULL;
void getscreen() {
	JNIEnv *screen_env;
	if (gs_jvm->GetEnv((void**) &screen_env, JNI_VERSION_1_4) != JNI_OK) {
		int status = gs_jvm->AttachCurrentThread((void **) &screen_env, NULL);
		if (status < 0) {
			return NULL;
		}
	}
	if (screen_env) {
		if (sshelper == 0) {
			sshelper = screen_env->FindClass(
					"com/gameassist/plugin/nativeutils/ScreenshotHelper");
		}
		if (sshelper == 0) {
			return;
		}
		if (method1 == 0) {
			method1 = screen_env->GetStaticMethodID(sshelper, "takeScreenshot",
					"()V");
		}
		if (method1 == 0) {
			return;
		}
		if (screenshot == 0) {
			screenshot = 1;
		} else {
			screen_env->CallStaticVoidMethod(sshelper, method1, NULL);
		}
	}
}
int (*old_IsValid)(int a1) = NULL; //截屏回调
int IsValid(int a1) {
	int ret = old_IsValid(a1);
	if (screenshot == 1) {
		getscreen();
		screenshot = 0;
	}
  return ret;
}
int (*old_InMap)(int a1) = NULL;
int InMap(int a1) {
	IsInGameing = 1;
	int ret = old_InMap(a1);
  return ret;
}
void doScreenshotCheat(JNIEnv *env,JavaVM* vm) {
	 gs_jvm=vm;
	 getscreen();
}

void doGameCheat(int flag,int arg1,int arg2){
	switch(flag){
	   /*case 1:{
		  // changeMode(arg2);
		   break;
	   }
	   case 18:{
		   iskill = arg2;
		   break;
	   }
	   case 2:{
		   containerdrop = arg2;
		   break;
	   }
	   default:{
		   break;
	   }*/
	}
}


int (*old_PlayerHurt)(int a1,int a2,int a3,int a4,int a5,int a6,int a7) = NULL;
int PlayerHurt(int a1,int a2,int a3,int a4,int a5,int a6,int a7) {
	if(lockNotAPlayer){
		return 0;
	}
	 int ret=old_PlayerHurt(a1,a2,a3,a4,a5,a6,a7);
   return ret;
}
int (*old_IsShoeAccessory)(int a1) = NULL;  //是否打败过暗影
int IsShoeAccessory(int a1) {
	 int ret = old_IsShoeAccessory(a1);
	 //LogD("<%s> ret=%d", __FUNCTION__,ret);
	return ret;
}
int wingArray[]={2280,2609,5035,2494,1866,1871,665,492,948,1162,761,823,1830,1586,1797,1165,1515,1583};
 int (*old_IsWing)(int a1) = NULL;
int IsWing(int a1) {
  /*  int rewriter=0;
    int player=old_GetPlayer();
	int itemtype=old_itemGetType(player+3596);
	for(int i=0;i<18;i++){
		if(itemtype==wingArray[i]){
			rewriter=1;
			break;
		}
	}*/
  //LogD("<%s>wingType=%d", __FUNCTION__, wingType);
//if(itemtype==0||rewriter!=0){
	if(wingType>-1){
		if(!old_IsShoeAccessory(a1)){
		int player=old_GetPlayer()+4284; //第一个配饰槽
		int sourceType=	*(int *)(player+0xa0);
		if(sourceType!=wingType){
		//LogD("<%s>sourceType=%d", __FUNCTION__, sourceType);
		*(int *)(player+0xa0)=wingType;
		if(wingType==0){
			wingType==-1;
		}
		}
		}
	}
    /*
     * 第二个配饰槽：3768
     * 第三个配饰槽：3940
     * 第四个配饰槽：4112
     * 第二个配饰槽：4284
     * */
	 int ret=old_IsWing(a1);
	// LogD("<%s>ret=%d", __FUNCTION__, ret);
   return ret;
}
int (*old_GetWingTime)(int a1) = NULL;
int GetWingTime(int a1) {
	 int ret=old_GetWingTime(a1);
	 //LogD("<%s>ret=%d", __FUNCTION__, ret);
	 if(lockFlyTimeMax==1){
	ret=	 0x79999999;
	 }

  return ret;
}
int (*old_UpdateHealth)(int HudState,int Player) = NULL;
int UpdateHealth(int HudState,int Player) {
	int ret=old_UpdateHealth(HudState,Player);
	if(isKillSelf==1){
			old_KillMe(Player,6,-1,0,-196353);
			isKillSelf=0;
			return old_UpdateHealth(HudState,Player);
		}
if(lockPlayerHP==1){
	if(Player){
	int maxHPValue=*(unsigned char *)(Player+27986);
	 int currHPValue=*(unsigned char *)(Player+0x6D54);
	 if(currHPValue!=maxHPValue){
	*(unsigned char *)(Player+0x6D54)=maxHPValue;
	 }
	/* int maxSPValue=*(unsigned char *)(Player+27998);
	 int currSpValue=*(unsigned char *)(Player+27992);
	 if(currSpValue!=maxSPValue){
	*(unsigned char *)(Player+27992)=maxSPValue; //当前蓝
	 }*/
	// LogD("<%s>currSpValue=%d maxSPValue=%d", __FUNCTION__, currSpValue,maxSPValue);
	}
}
if(lockPlayerMP==1){
	if(Player){
	 int maxSPValue=*(unsigned char *)(Player+27998);
	 int currSpValue=*(unsigned char *)(Player+27992);
	 if(currSpValue!=maxSPValue){
	*(unsigned char *)(Player+27992)=maxSPValue; //当前蓝
	 }
	// LogD("<%s>currSpValue=%d maxSPValue=%d", __FUNCTION__, currSpValue,maxSPValue);
	}
}

  return ret;
}

int (*old_CheckValentine)(int a1) = NULL;
int CheckValentine(int a1) {
	 int ret=old_CheckValentine(a1);  //情人节
	 if(festiveOder==2){
	 		ret=1;
	 	}
 return ret;
}
int (*old_CheckSaintPatrickDay)(int a1) = NULL;
int CheckSaintPatrickDay(int a1) {  //圣帕特里克节
	 int ret=old_CheckSaintPatrickDay(a1);
	 if(festiveOder==3){
	 		ret=1;
	 	}
 return ret;
}
int (*old_CheckEaster)(int a1) = NULL;
int CheckEaster(int a1) {  //复活节
	 int ret=old_CheckEaster(a1);
	 if(festiveOder==4){
	 		ret=1;
	 	}
 return ret;
}
int (*old_CheckOctoberFest)(int a1) = NULL;
int CheckOctoberFest(int a1) {   //啤酒节
	 int ret=old_CheckOctoberFest(a1);
	 if(festiveOder==5){
	 		ret=1;
	 	}
 return ret;
}
int (*old_CheckHalloween)(int a1) = NULL;
int CheckHalloween(int a1) {   //万圣节
	 int ret=old_CheckHalloween(a1);
	 if(festiveOder==6){
	 		ret=1;
	 	}
 return ret;
}
int (*old_CheckThanksgiving)(int a1) = NULL;
int CheckThanksgiving(int a1) {   //感恩节
	 int ret=old_CheckThanksgiving(a1);
	 if(festiveOder==7){
	 		ret=1;
	 	}
 return ret;
}
int (*old_CheckXMas)(int a1) = NULL;
int CheckXMas(int a1) {   //圣诞节
	 int ret=old_CheckXMas(a1);
	 if(festiveOder==8){
	 		ret=1;
	 	}
 return ret;
}
int (*old_CheckNewYear)(int a1) = NULL;
int CheckNewYear(int a1) {   //新年
	 int ret=old_CheckNewYear(a1);
	if(festiveOder==1){
		ret=1;
	}
 return ret;
}
int (*old_CheckTU5ReleaseGift)(int a1) = NULL;
int CheckTU5ReleaseGift(int a1) {   //礼物释放
	 int ret=old_CheckTU5ReleaseGift(a1);
	 /*LogD("<%s> ret=%d", __FUNCTION__, ret);
	  ret=1;*/
 return ret;
}
/*int (*old_CheckLunarNewYear)(int a1) = NULL;
int CheckLunarNewYear(int a1) {   //礼物释放
	 int ret=old_CheckLunarNewYear(a1);
	 LogD("<%s> ret=%d", __FUNCTION__, ret);
	  ret=1;
 return ret;
}*/
long GetTick(char *str_time)
{
    struct tm stm;
    int iY, iM, iD, iH, iMin, iS;
    memset(&stm,0,sizeof(stm));
    iY = atoi(str_time);
    iM = atoi(str_time+5);
    iD = atoi(str_time+8);
    iH = atoi(str_time+11);
    iMin = atoi(str_time+14);
    iS = atoi(str_time+17);

    stm.tm_year=iY-1900;
    stm.tm_mon=iM-1;
    stm.tm_mday=iD;
    stm.tm_hour=iH;
    stm.tm_min=iMin;
    stm.tm_sec=iS;
    return mktime(&stm);
}


int toNightFlag=100;
static int lockTimeValue=0;

 int (*old_Timeupdate)(int a1) = NULL;  //需要做白天夜晚场景切换是此函数返回1;
int Timeupdate(int a1) {
	gameTimeValue=a1;
   int currTimeValue	=*(int *)(a1+4); //this[1]
	if(lockCurrTime==1){
		*(int *)(a1+4)=lockTimeValue; //锁定当前时间
	}else{
		if(timeSpeed>0){
		int timesub=currTimeValue-lockTimeValue;
		currTimeValue=lockTimeValue+(timesub+256*timeSpeed);
	     *(int *)(a1+4)=currTimeValue;
	   //  LogD("<%s> timeSpeed=%d", __FUNCTION__,*(int *)(a1+4));
		}
		lockTimeValue=currTimeValue;
		// LogD("<%s> currTimeValue=%d", __FUNCTION__,*(int *)(a1+4));
	}
	if(changeToDay==1){
		old_timeReset(a1,0.01);
		// LogD("<%s> currTimeValue=%d", __FUNCTION__,*(int *)(a1+4));
		 changeToDay=0;
	}else if(changeToNight==1){
		old_timeReset(a1,0.01);
		*(int *)(a1+4)=1209840512;
		// LogD("<%s> changeToNight=%d", __FUNCTION__,*(int *)(a1+4));
		changeToNight=0;
	}
	  int ret=old_Timeupdate(a1);
	//  LogD("<%s> ret=%d", __FUNCTION__,ret);
  return ret;
}

 int (*old_UpdateRain)(int a1,int a2) = NULL;
int UpdateRain(int a1,int a2) {
	 int ret=old_UpdateRain(a1,a2);
	 int raining=*(int *)(baseA+0x6121D8);
	int rainTime=*(int *)(baseA+0x6121D4);
	//int maxRaining=*(int *)(baseA+0x6121D0);

	int Clouds=1061997772;
	if(isRaining==1){
	//LogD("<%s> isRaining=%d", __FUNCTION__,rainTime);
	//*(int *)(baseA+0x6121D4)=512;
	// old_ChangeRain(0);
	 old_StartRain(0);
	 }else if(isRaining==0){
	 *(int *)(baseA+0x6121D4)=0;
	 isRaining=-1;
	 }
	 if(haveLightning==1){
	 //LogD("<%s> isRaining=%d", __FUNCTION__,rainTime);
	// *(int *)(baseA+0x6121D4)=512;
	 old_StartRain(0);
	 old_UpdateLightning(rainTime);
	 }else if(haveLightning==0){
	 *(int *)(baseA+0x6121D4)=0;
	 haveLightning=-1;
		 }
	 if(haveLightning||isRaining){
		 old_UpdateClouds(1,Clouds,512);
	 }
     if(noRain==1){
      //LogD("<%s> noRain=%d", __FUNCTION__,noRain);
	 *(int *)(baseA+0x6121D4)=0;
	 *(int *)(baseA+0x6121D8)=0;
	 }


 return ret;
}


/*
 * 安全领域
 */
int (*old_npcIsFriendly)(int a1) = NULL;
int npcIsFriendly(int a1) {
	 int ret=old_npcIsFriendly(a1);
	 if(lockSArea==1){
		 return 1;
	 }else {
		 return ret;
	 }
}
int (*old_TransitionToNight)(int a1) = NULL;
int TransitionToNight(int a1) {

	int ret = old_TransitionToNight(a1);
	// LogD("<%s> ret=%d", __FUNCTION__,ret);
	return ret;
}
int (*old_IsGoblinInvasionAllowed)(int a1) = NULL;  //是否入侵
int IsGoblinInvasionAllowed(int a1) {
	 int ret = old_IsGoblinInvasionAllowed(a1);
	//  LogD("<%s> ret=%d", __FUNCTION__,ret);
	return ret;
}
int (*old_CanStartInvasion)() = NULL;  //能否入侵
int CanStartInvasion() {
	 int ret = old_CanStartInvasion();
	//  LogD("<%s> ret=%d", __FUNCTION__,ret);
	return ret;
}
int (*old_OpenCrateItem)(int player,int item) = NULL;  //开箱子
int OpenCrateItem(int player,int item) {
	 int ret = old_OpenCrateItem(player,item);
	 // LogD("<%s> ret=%d", __FUNCTION__,ret);
	return ret;
}
int (*old_chestIsLocked)(int player,int a2) = NULL;  //箱子是否解锁
int chestIsLocked(int player,int a2) {
	 int ret = old_chestIsLocked(player,a2);
	// LogD("<%s> ret=%d", __FUNCTION__,ret);
	return ret;
}

int (*old_TabInventoryGetItem)(int TabInventory,int a2) = NULL;  //解锁箱子  仓库大小为48个
int TabInventoryGetItem(int TabInventory,int a2) {
	//LogD("<%s> a2=%d", __FUNCTION__,a2);
	 int ret = old_TabInventoryGetItem(TabInventory,a2);
	/* int type=old_itemGetType(ret);
	 int item=(char *)old_GetPlayer() + 172 * a2 + 4972;
	 int stack=old_GetStack(ret);
	 LogD("<%s> ret=%d item=%d stack=%d", __FUNCTION__,ret,item,stack);
	  LogD("<%s> type=%d id=%d", __FUNCTION__,type,a2);*/
	//  ret=0;
	return ret;
}
int (*old_DecreaseStack)(int item, int a2) = NULL;  //减栈
int DecreaseStack(int item, int a2) {
	 int ret = old_DecreaseStack(item,a2);
	//  LogD("<%s>  a2=%d ret=%d", __FUNCTION__, a2,ret);
	return ret;
}
int (*old_IncreaseStack)(int item, int a2) = NULL;  //加栈
int IncreaseStack(int item, int a2) {
	 int ret = old_IncreaseStack(item,a2);
	// LogD("<%s>  a2=%d ret=%d", __FUNCTION__, a2,ret);
	return ret;
}
int (*old_CountItems)(int Inventory, int a2) = NULL;  //计数器
int CountItems(int Inventory, int a2) {
	 int ret = old_CountItems(Inventory,a2);
	// LogD("<%s> Inventory=%d a2=%d ret=%d", __FUNCTION__,Inventory,a2,ret);
	//   ret=1;
	return ret;
}

//int (*old_FindItem)(int player, int type) = NULL;  //从仓库中查找有没有指定类型的物品。返回对应仓库id
/*int FindItem(int player, int type) {
	 int ret = old_FindItem(player,type);
	  LogD("<%s> player=%d type=%d ret=%d", __FUNCTION__,player,type,ret);
	//   ret=1;
	return ret;
}*/
int (*old_hasItemInInventory)(int player, int type) = NULL;  //从仓库中查找有没有指定类型的物品（返回0或者1）。
int hasItemInInventory(int player, int type) {
	 int ret = old_hasItemInInventory(player,type);
	  //LogD("<%s> player=%d type=%d ret=%d", __FUNCTION__,player,type,ret);
	//   ret=1;
	return ret;
}
int (*old_HasItem)(int player, int type) = NULL;  //从仓库中查找有没有指定类型的物品（返回0或者1）。
int HasItem(int player, int type) {
	 int ret = old_HasItem(player,type);
	//  LogD("<%s> player=%d type=%d ret=%d", __FUNCTION__,player,type,ret);
	//   ret=1;
	return ret;
}


const static HOOK_SYMBOL gHookSymbols[] = {

 {"_ZN6Player4HurtEiibbyb", (void *)&PlayerHurt,      (void **)&old_PlayerHurt},  //无敌
 {"_ZNK4Item6IsWingEv", (void *)&IsWing,      (void **)&old_IsWing},  //翅膀
 {"_ZN6Player11GetWingTimeEv", (void *)&GetWingTime,      (void **)&old_GetWingTime},  //翅膀飞行时间
 {"_ZN8HudState26UpdateHealthAndManaWidgetsEP6Player", (void *)&UpdateHealth,      (void **)&old_UpdateHealth},  //无敌时加满血量 自杀
 {"_ZNK19GLTextureIdentifier7IsValidEv", (void *)&IsValid,      (void **)&old_IsValid},  //截屏及;
 {"_ZN16GameStateManager5InMapEv", (void *)&InMap,      (void **)&old_InMap},  //是否在地图;

 {"_ZN4Time14CheckValentineEv", (void *)&CheckValentine,      (void **)&old_CheckValentine},  //检测情人节
 {"_ZN4Time20CheckSaintPatrickDayEv", (void *)&CheckSaintPatrickDay,      (void **)&old_CheckSaintPatrickDay},  //检测圣帕特里克日
 {"_ZN4Time11CheckEasterEv", (void *)&CheckEaster,      (void **)&old_CheckEaster},  //检测复活节节
 {"_ZN4Time16CheckOctoberFestEv", (void *)&CheckOctoberFest,      (void **)&old_CheckOctoberFest},  //检测啤酒节
 {"_ZN4Time14CheckHalloweenEv", (void *)&CheckHalloween,      (void **)&old_CheckHalloween},  //检测万圣节
 {"_ZN4Time17CheckThanksgivingEv", (void *)&CheckThanksgiving,      (void **)&old_CheckThanksgiving},  //检测感恩节
 {"_ZN4Time9CheckXMasEv", (void *)&CheckXMas,      (void **)&old_CheckXMas},  //检测圣诞节
 {"_ZN4Time12CheckNewYearEv", (void *)&CheckNewYear,      (void **)&old_CheckNewYear},  //检测新年

 {"_ZN4Time19CheckTU5ReleaseGiftEv", (void *)&CheckTU5ReleaseGift,      (void **)&old_CheckTU5ReleaseGift},  //给予物品
 //{"_ZN4Time17CheckLunarNewYearEv", (void *)&CheckLunarNewYear,      (void **)&old_CheckLunarNewYear},  //检测农历新年 此函数无法hook

  //{"_ZN6Player7DoCoinsEi", (void *)&AddSortItem,      (void **)&old_AddSortItem},  //捡钱
//  {"_ZN9Inventory12TryEquipItemEP4ItemS1_", (void *)&AddSortItem,      (void **)&old_AddSortItem},  //切换装备
 // {"_ZN4Time6updateEv", (void *)&AddSortItem,      (void **)&old_AddSortItem},  //切换装备
 {"_ZN4Time6updateEv", (void *)&Timeupdate,      (void **)&old_Timeupdate},  //更新时间切换白天黑夜
 {"_ZN7Weather10UpdateRainERK4Time", (void *)&UpdateRain,      (void **)&old_UpdateRain},  //更新雨天
 {"_ZNK3NPC10IsFriendlyEv", (void *)&npcIsFriendly,      (void **)&old_npcIsFriendly},  //安全领域  危险生物凋谢;
 {"_ZN4Main17TransitionToNightEv", (void *)&TransitionToNight,      (void **)&old_TransitionToNight}, //无用函数
 {"_ZN4Main16CanStartInvasionEN12InvasionType4EnumE", (void *)&CanStartInvasion,      (void **)&old_CanStartInvasion},
 {"_ZN6Player23IsGoblinInvasionAllowedEv", (void *)&IsGoblinInvasionAllowed,      (void **)&old_IsGoblinInvasionAllowed},
 {"_ZN6Player15IsShoeAccessoryEN9ItemTypes4EnumE", (void *)&IsShoeAccessory,      (void **)&old_IsShoeAccessory},
// {"_ZN6Player10TryBuyItemEi", (void *)&TryBuyItem,      (void **)&old_TryBuyItem},//内含金钱
// {"_ZN6Player9OpenCrateEP4Item", (void *)&OpenCrateItem,      (void **)&old_OpenCrateItem},
 // {"_ZN5Chest8IsLockedEii", (void *)&chestIsLocked,      (void **)&old_chestIsLocked},
 //{"_ZN6Player11CanUseChestEv", (void *)&CanUseChest,      (void **)&old_CanUseChest},
 // {"_ZN6Player16InteractWithTileEii", (void *)&ChestUnlock,      (void **)&old_ChestUnlock},
 //{"_ZN6Player16InteractWithTileEii", (void *)&InteractWithTile,      (void **)&old_InteractWithTile},
//{"_ZNK10ItemWidget7GetItemEv", (void *)&GetNearbyChest,      (void **)&old_GetNearbyChest},
 {"_ZN12TabInventory9GetItemAtEi", (void *)&TabInventoryGetItem,      (void **)&old_TabInventoryGetItem},
 {"_ZN9Inventory10CountItemsEi", (void *)&CountItems,      (void **)&old_CountItems},

 {"_ZN4Item13DecreaseStackEs", (void *)&DecreaseStack,      (void **)&old_DecreaseStack},
 {"_ZN4Item13IncreaseStackEs", (void *)&IncreaseStack,      (void **)&old_IncreaseStack},

 {"_ZN6Player18hasItemInInventoryEi", (void *)&hasItemInInventory,      (void **)&old_hasItemInInventory},//从仓库中查找有没有指定类型的物品(返回0或1)。
 {"_ZNK6Player7HasItemEi", (void *)&HasItem,      (void **)&old_HasItem},//从仓库中查找有没有指定类型的物品(返回0或1)。








};

const static FIND_SYMBOL gFindSymbols[] = {
	    {"_ZN6Player6KillMeEfiby",            (void **)&old_KillMe},  //杀死自己
	    {"_ZN6Player9GetPlayerEv",            (void **)&old_GetPlayer},  //获取player
	    {"_ZN4ItemC2Ev",            (void **)&old_itemItem},  //init item
	    {"_ZN4Item4InitEv",            (void **)&old_itemInit},  //init item

	    {"_ZN4Time5resetEf",            (void **)&old_timeReset},  //设置到白天
	    {"_ZN4Time11updateNightEv",            (void **)&old_updateNight},  //设置为晚上
	    {"_ZN7Weather10ChangeRainEv",            (void **)&old_ChangeRain},  //变为下雨天气
	    {"_ZN7Weather9StartRainEv",            (void **)&old_StartRain},  //变为下雨天气


	    {"_ZN7Weather12UpdateCloudsEv", 		(void **)&old_UpdateClouds},  //更新乌云闪电
	    {"_ZN7Weather15UpdateLightningEv", 	(void **)&old_UpdateLightning},  //更新闪电
	     {"_ZN4Time14startBloodMoonEv", 	(void **)&old_startBloodMoon},  //血月
	     {"_ZN6Player17SunMoonTransitionEb", 	(void **)&old_sunMoonTransition},  //过度
	     {"_ZN3NPC17SpawnHardModeBossEv", 	(void **)&old_SpawnHardModeBoss},  //boos模式
	     {"_ZN4Time16startPumpkinMoonEv", 	(void **)&old_startPumpkinMoon},  //开启南瓜月模式
	     {"_ZN4Time15stopPumpkinMoonEv", 	(void **)&old_stopPumpkinMoon},  //关闭南瓜月模式
	     {"_ZN4Time12startEclipseEv", 	(void **)&old_startEclipse},  //开启日食

	     {"_ZN4Time14startFrostMoonEv", 	(void **)&old_startFrostMoon},  //霜月开
	     {"_ZN4Time13stopFrostMoonEv", 	(void **)&old_stopFrostMoon},  //霜月关

	     {"_ZN4Main13StartInvasionEN12InvasionType4EnumE", 	(void **)&old_StartInvasion},  //开始入侵
	     {"_ZN4Main13GetMainPlayerEv", 	(void **)&old_GetMainPlayer},
	     {"__floatsisf", 	(void **)&old_floatsisf},
	     {"_ZNK4Item7GetTypeEv", 	(void **)&old_itemGetType},
	     {"_ZN4Item7SetTypeEN9ItemTypes4EnumE", 	(void **)&old_itemsetType},
	     {"_ZN6Player22AddItemToCraftingListsER4Item", 	(void **)&old_AddItemToCraftingLists},
	     {"_ZN9Inventory4CopyEP4ItemS1_", 	(void **)&old_CopyItem},
	     {"_ZN4Item7NewItemEiiiiiibib", 	(void **)&old_NewItem},
	     {"_ZN4Item8SetStackEs", 	(void **)&old_SetStack},
	     {"_ZNK4Item8GetStackEv", 	(void **)&old_GetStack},
	     {"_ZN4Item11SetDefaultsEiib",   (void **)&old_SetDefaults},//设置默认值
	     {"_ZN9Inventory15CountEmptySlotsEv",   (void **)&old_CountEmptySlots},//仓库空槽数量
	     {"_ZN6Player14ClearInventoryEv",   (void **)&old_ClearInventory},//仓库空槽数量
	     {"_ZN9Inventory8FindItemERK6PlayerN9ItemTypes4EnumE", (void **)&old_FindItem},//从仓库中查找有没有指定类型的物品。




















};

void hook_symbols(soinfo * handle) {

#ifdef NDK_DEBUG
	LogD("<%s> %s %s handle = %x( funcs = %d)", __FUNCTION__, __DATE__,__TIME__,(int)handle, sizeof(gHookSymbols)/sizeof(gHookSymbols[0]));
#endif
	baseA=handle->base;
	for (int i = 0; i < sizeof(gFindSymbols) / sizeof(gFindSymbols[0]); i++) {
		FIND_SYMBOL find = gFindSymbols[i];
		*find.func = dlsym(handle, find.symbol);
#ifdef NDK_DEBUG
		LogD("<HookSymbol> symbol = 0x%x : %s", (int)*find.func, find.symbol);
#endif
	}

	for (int i = 0; i < sizeof(gHookSymbols) / sizeof(gHookSymbols[0]); i++) {
		HOOK_SYMBOL hook = gHookSymbols[i];
		inlineHookSymbol(handle, hook.symbol, hook.new_func, hook.old_func);

#ifdef NDK_DEBUG
		LogD("<HookSymbol> symbol = %s(%x), new = 0x%x, old = 0x%x", hook.symbol, (int)getAddress(handle,hook.symbol ),(int)hook.new_func, (int)*hook.old_func);
#endif
	}

}
