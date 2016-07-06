#include "hookutils.h"

int (*completeGola)(int r0) = NULL;

void doProcessCheat(int flag, int arg1, int arg2) {

}

char* join3(char *s1, char *s2) {
	char *result = malloc(strlen(s1) + strlen(s2) + 1); //+1 for the zero-terminator
	//in real code you would check for errors in malloc here
	if (result == NULL)
//		exit(1);

		strcpy(result, s1);
	strcat(result, s2);

	return result;
}

char* Trim(char* lpStr) {
	if (lpStr == NULL)    //如果为空则返回NULL
		return NULL;

	char tmp[0x200] = { 0 };    //临时保存
	strcpy(tmp, lpStr);
	char* ptr = tmp;
	ptr = tmp + strlen(tmp) - 1;    //定位到最后一个字符
	while (*ptr == 0x20)    //从最后开始删除
	{
		ptr--;
		if (ptr < tmp)    //全部是空格则退出
				{
			strcpy(lpStr, "");
			return lpStr;
		}
	}
	*(ptr + 1) = 0;    //截断后面的空格
	ptr = tmp;
	while (*ptr == 0x20)    //从前面开始删除
		ptr++;
	strcpy(lpStr, ptr);    //返回去掉空格后字符串
	return lpStr;
}

char * GetnameFilename(const char* fullpathname) {
	char* save_name, *pos;
	int name_len;
	name_len = strlen(fullpathname);
	pos = fullpathname + name_len;
	while (*pos != '/' && pos != fullpathname)
		pos--;
	if (pos == fullpathname) {
		save_name = fullpathname + 1;
//LogD("<%s> filename=%s", __FUNCTION__, save_name);
		return save_name;
	}
	name_len = name_len - (pos - fullpathname);
	save_name = (char*) malloc(name_len + 1);
	memcpy(save_name, pos + 1, name_len);
//LogD("<%s> filename=%s", __FUNCTION__, save_name);
	return save_name;
}
int dump = 1;
int i = 0;

int (*old_my_luaL_loadbuffer)(void *L, const char *buff, size_t sz,
		const char *name) = NULL;
int my_luaL_loadbuffer(void *L, const char *buff, size_t sz, const char *name) {
//	LogD("<%s> name=%s", __FUNCTION__, name);
	LogD("<%s> buff=%s", __FUNCTION__, buff);
//	LogD("<%s> sz=%s", __FUNCTION__, sz);
//	char arg[] = "123456789qwertyuiopasdfghjklzxcvbnm";
//	i = i + 1;
//	char *p = arg[i];
//printf("%s\n",&p);
//	if (dump) {
//		char *filename = GetnameFilename(name);
//		LogD("<%s> 文件名=%s", __FUNCTION__, filename);
//		FILE * stream;
//		char *targetpath = "/sdcard/wz/";
//		int retlen = strlen(targetpath) + strlen(name) + 1;
//		char * result = (char *) malloc(retlen);
//		strcpy(result, targetpath);
//		strcat(result, filename);
//		LogD("<%s> 开始写入文件=%s", __FUNCTION__, result);
//		if ((stream = fopen(result, "wb+")) == NULL) {
//			LogD("<%s> null:%s", __FUNCTION__, "null");
//		} else {
//			LogD("<%s> writer:%s", __FUNCTION__,
//					"/sdcard/wz/ starting writer flie");
//			int ret = fwrite(buff, sz, 1, stream);
//			if (ret) {
//				LogD("<%s> 文件写入完毕=%s", __FUNCTION__, result);
//			}
//			fclose(stream);
//		}
//	}
//	char *filename = GetnameFilename(name);
//	 if (strcmp(filename, "BasicHeroPlayerController")==0) {
//		//  LogD("<%s> name=%s", __FUNCTION__, name);
//		// LogD("<%s> buff=%s", __FUNCTION__, buff);
//		 char *rep="RegisterLUAOnlyClass(\"ITD2BasicHeroPlayerController\", \"ITD2BasicPlayerController\");\r\n-------------------------------------------------------------------------------------------------\r\n-- class for funcionality common both for hero, multihero and antihero\r\n-- TODO: playercontroller hierarchy is getting too big, things such as abilities, resources\r\n-- should be encapsulated as components, so there is no need for common base class\r\n\r\n-------------------------------------------------------------------------------------------------\r\nITD2BasicHeroPlayerController.DefaultAbilityCooldownClock = \"AbilityCooldownClock\"\r\n\r\n-------------------------------------------------------------------------------------------------\r\nITD2BasicHeroPlayerController.Event_ResourcesChanged = \"ITD2BasicHeroPlayerController.Event_ResourcesChanged\"\r\nITD2BasicHeroPlayerController.Event_AbilityChargesChanged = \"ITD2BasicHeroPlayerController.Event_AbilityChargesChanged\"  -- TODO: CONSOLIDATE!\r\nITD2BasicHeroPlayerController.Event_AbilityChargesAdded = \"ITD2BasicHeroPlayerController.Event_AbilityChargesAdded\" -- TODO: CONSOLIDATE!\r\nITD2BasicHeroPlayerController.Event_HACK_DontShowChecked = \"ITD2BasicHeroPlayerController.Event_HACK_DontShowChecked\"\r\n\r\n-------------------------------------------------------------------------------------------------\r\nITD2BasicHeroPlayerController.ABILITYCHARGES_CAST = \"ITD2BasicHeroPlayerController.ABILITYCHARGES_CAST\"\r\nITD2BasicHeroPlayerController.ABILITYCHARGES_PICKUPCOLLECTED = \"ITD2BasicHeroPlayerController.ABILITYCHARGES_PICKUPCOLLECTED\"\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- HACKS\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: game logic needs to know something from UI layer...\r\nfunction ITD2BasicHeroPlayerController:HACK_DontShowChecked(panelName, checked) \r\n	self:SendEvent(ITD2BasicHeroPlayerController.Event_HACK_DontShowChecked, {panelName = panelName, checked = checked});\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- CONSTRUCTION AND DESTRUCTION\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:Constructor() \r\n	local instance = ITD2BasicPlayerController:Constructor()\r\n	setmetatable(instance, ITD2BasicHeroPlayerController)\r\n	instance.resources = 0\r\n	instance.initialResources = 0 -- TODO: cleanup\r\n	instance.showResources = true\r\n\r\n	instance.abilities = nil\r\n	instance.abilityGlobalDisableCounter = RefCounter:Constructor();\r\n	instance.abilityGroupDisableCounters = {}\r\n\r\n	instance.tacticalBlocked = false -- TODO: switch to RefConunter!\r\n	instance.tacticalEnterBlocked = false;\r\n    instance.tacticalExitBlocked = false;\r\n	instance.unconsciousCounter = 0\r\n	return instance\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:DoInit()\r\n	ITD2BasicPlayerController.DoInit(self)\r\n	\r\n	self.abilityCooldownClock = gGame.HQ:GetClock(self.externalParams.abilityCooldownClock or self.DefaultAbilityCooldownClock);\r\n	self.unconsciousCounter = 0\r\n	\r\n	self:CreateTechnologyManager();\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:DoDelete()\r\n	self:DeleteTechnologyManager()\r\n	ITD2BasicPlayerController.DoDelete(self)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:DoReset()\r\n	self:DeleteTechnologyManager();\r\n	\r\n	-- TODO: DIRTY!\r\n	-- remove all blockades\r\n	self.abilityGlobalDisableCounter:Reset();\r\n	\r\n	self.abilities = nil;\r\n	self.abilityGroupDisableCounters = {};\r\n	\r\n	self.tacticalEnterBlocked = false;\r\n    self.tacticalExitBlocked = false;\r\n    -- HACK HACK HACK we need to do it now, becouse gGame:Reset will SetGameLevel now, this is not delayed\r\n    if self.remote then\r\n        self.remote:Hack_RemoveTacticalBlockades()\r\n    end\r\n	\r\n	ITD2BasicPlayerController.DoReset(self)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:SetConfig(config) \r\n	assert(config.Resources)\r\n	assert(config.Abilities)\r\n	self.initialResources = config.Resources; assert(self.initialResources);\r\n	self:SetResources(config.Resources)\r\n	self:SetAbilities(config.Abilities)\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:ShowResources(flag)\r\n    self.showResources = flag\r\n    self.remote:ShowResources(flag)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- ABILITIES\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:SetAbilities(abilities)\r\n	if #(abilities) > igHeroMaxAbilitiesNumber then\r\n		printWarning(self,\": too many hero abilities\")\r\n		return\r\n	end\r\n\r\n	self.abilities={};\r\n	\r\n	for i,entry in ipairs(abilities) do\r\n		local abilityName = entry.Name; assert(abilityName);\r\n		local abilityParams = GetAbilityParams(abilityName); assert(abilityParams);\r\n		\r\n		-- pick ability group\r\n		local abilityGroup = abilityParams.Group; assert(abilityGroup);\r\n		local abilityGroupDisableCounter = self.abilityGroupDisableCounters[abilityGroup];\r\n		\r\n		if not abilityGroupDisableCounter then\r\n			abilityGroupDisableCounter = RefCounter:Constructor(); -- TODO: RefCounter:Constructor(self.abilityGlobalDisableCounter); ?\r\n			self.abilityGroupDisableCounters[abilityGroup] = abilityGroupDisableCounter;\r\n		end\r\n		\r\n		assert(abilityGroupDisableCounter);\r\n		\r\n		local newEntry={};\r\n		table.insert(self.abilities, newEntry);\r\n		newEntry.name = abilityName;\r\n		newEntry.params = abilityParams;	\r\n		newEntry.disableCounter = RefCounter:Constructor(abilityGroupDisableCounter);\r\n		\r\n		self:SetAbilityId(i, GetAbilityIdByName(abilityName));\r\n		self:SetAbilityChargesNumber(i, (abilityParams.Infinite and 1) or entry.Charges or 0);\r\n		self:SetAbilityCooldown(i, 0, true);\r\n		self:DisableAbility(i, igParams.Blockades.LevelBlockade, entry.Disabled);\r\n		self:ShowAbility(i, not entry.Hidden);\r\n	end;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO - ta i inne akcje sterujace zdolnosciami powinny zostac przerobione tak, zeby nie oczekiwac indeksu a nazwy zdolnosci <-- Anomaly 1 comment\r\n-- TODO: everything should accept abilityId instead of index (which can theoretically change) <-- Anomaly 2 comment\r\n-- NOTE: Anomaly 3 it's your move :p\r\nfunction ITD2BasicHeroPlayerController:ActivateSpecialAbility(index, position, target)\r\n	local abilityEntry=self.abilities[index];\r\n	\r\n	-- cast conditions\r\n	if self.abilityGlobalDisableCounter:IsLocked()\r\n		or not abilityEntry \r\n		or fle(abilityEntry.charges, 0)\r\n		or fgt(abilityEntry.cooldown, 0)\r\n		or abilityEntry.disableCounter:IsLocked() \r\n	then\r\n		return false;\r\n	end\r\n		\r\n	-- TODO: cast range checking (after finalizing movement replication, right now its senseless)\r\n    if target and not target.Owner then\r\n        -- Owner disconnected already, we have to skip \r\n        return false\r\n    end\r\n\r\n	local realtarget = (target and target.Owner) or nil; \r\n	local abilityIdx, ability = gGame.HQ:CreateAbility(abilityEntry.name, hero, self, position, realtarget);\r\n		\r\n	if not ability then\r\n		-- NOTE: this can happen when remote client wants to cast ability on already dead target\r\n		return false;\r\n	end\r\n		\r\n	if not abilityEntry.params.Infinite then\r\n		self:AddAbilityChargesNumber(index, -1, {tag=ITD2BasicHeroPlayerController.ABILITYCHARGES_CAST}); -- TODO: cleanup\r\n	end\r\n\r\n    local cooldown = abilityEntry.params.Cooldown and 1 or 0\r\n    self:SetAbilityCooldown(index, cooldown, true)\r\n	self:SendEvent(Game.GameEvent_AbilityCasted, {index = index, ability = ability})\r\n\r\n	local hero = self:GetControlledEntity()\r\n    if hero then\r\n        hero:OnAbilityCasted()\r\n    end\r\n\r\n	return true;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:KillAbility(instanceIndex)\r\n    local ability=gGame.HQ:GetAbility(instanceIndex);\r\n    self:SendEvent(Game.GameEvent_AbilityWillBeKilled, ability)\r\n    gGame.HQ:DeleteAbility(instanceIndex, true)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- this method simply updates cooldowns and pushes them to remote controller\r\n-- it could be done more sophisticaed and less bandwidth consuming, but it would require more work\r\nfunction ITD2BasicHeroPlayerController:UpdateAbilities()\r\n	if self.abilities and self.AbilityCooldownTimeMultiplier then\r\n		-- TODO: shouldnt cooldownTimeMultiplier be implemented in clock? or cooldowns scaled upon _entitymutated?\r\n		local elapsed = self.abilityCooldownClock:GetElapsedGameTime() * self.AbilityCooldownTimeMultiplier; \r\n		for i,ability in ipairs(self.abilities) do\r\n			if ability.params.Cooldown then\r\n				local cooldown = math.max(0, ability.cooldown - elapsed / ability.params.Cooldown);\r\n				self:SetAbilityCooldown(i, cooldown);\r\n			else\r\n				assert(feq(ability.cooldown, 0));\r\n			end\r\n		end\r\n	end\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:BlockAbilities(reason, flag)\r\n    self.abilityGlobalDisableCounter:Lock(reason, flag);\r\n	self.remote:BlockAbilities(self.abilityGlobalDisableCounter:IsLocked())\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:SetAbilityId(index,id)\r\n	assert(index<=#(self.abilities))\r\n	self.abilities[index].id=id;\r\n	self.remote:SetAbilityId(index, id)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetAbilityData(index)\r\n    return self.abilities[index]\r\nend\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetAbilityChargesNumber(index)\r\n	if index<=#(self.abilities) then\r\n		return self.abilities[index].charges;\r\n	end;\r\n	\r\n	return 0;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetAbilityCharges(abilityName)\r\n	for _,entry in pairs(self.abilities) do\r\n		if entry.name == abilityName then\r\n			return entry.charges\r\n		end\r\n	end\r\n	return 0\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:IterateAbilities(callback)\r\n	for idx,entry in pairs(self.abilities) do\r\n        callback(idx, entry)\r\n	end\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: index guards and unchecked return values\r\nfunction ITD2BasicHeroPlayerController:SetAbilityChargesNumber(index, charges)\r\n	if index > #(self.abilities) then\r\n		return false;\r\n	end;\r\n	\r\n	if charges > self.externalParams.MaxAbilityCharges then\r\n		charges = self.externalParams.MaxAbilityCharges;\r\n	end\r\n		\r\n	self.abilities[index].charges = charges;\r\n	self.remote:SetAbilityCharges(index, charges)\r\n	\r\n	self:SendEvent(ITD2BasicHeroPlayerController.Event_AbilityChargesChanged, {index = index, charges = charges});\r\n	return true;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:AddAbilityChargesNumber(index, delta, reason)\r\n	assert(self.abilities[index])\r\n	assert(not self.abilities[index].params.Infinite)\r\n	self:SendEvent(ITD2BasicHeroPlayerController.Event_AbilityChargesAdded, {index = index, charges = delta, reason = reason});\r\n	gGame.HQ.SeqSystem:OnEmitCustomEvent(gGame.HQ, \"AddAbilityChargesNumber\", {index = index, charges = delta, reason = reason}); -- HACK: 1) HQ REFERENCE 2) REDUNDANT WITH SEND EVENT\r\n	\r\n	self:SetAbilityChargesNumber(index, self:GetAbilityChargesNumber(index) + delta)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:SetAbilityCooldown(index, cooldown, breakLerp)\r\n	assert(index<=#(self.abilities))\r\n	self.abilities[index].cooldown = cooldown;\r\n	self.remote:SetAbilityCooldown(index, cooldown, breakLerp);\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetAbilityCooldown(index)\r\n    assert(index<=#(self.abilities))\r\n	return self.abilities[index].cooldown;\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: pass blockade reason\r\nfunction ITD2BasicHeroPlayerController:DisableAbility(index, blockade, flag)\r\n	if index > #(self.abilities) then\r\n		return false;\r\n	end;\r\n	\r\n	self.abilities[index].disableCounter:Lock(blockade, flag);\r\n	self.remote:SetAbilityBlockade(index, self.abilities[index].disableCounter:GetBlockade())\r\n	return true;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: pass blockade reason\r\nfunction ITD2BasicHeroPlayerController:IsAbilityDisabled(index)\r\n	if index > #(self.abilities) then\r\n		return true;\r\n	end;\r\n	\r\n	return self.abilities[index].disableCounter:IsLocked();\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:ShowAbility(index,flag)\r\n	if index<=#(self.abilities) then\r\n		if flag~=self.abilities[index].visible then\r\n			self.abilities[index].visible = flag;\r\n			self.remote:SetAbilityVisible(index, flag)\r\n		end;\r\n		\r\n		return true;\r\n	end;\r\n	\r\n	return false;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:IsAbilityVisible(index)\r\n	if index<=#(self.abilities) then\r\n		return self.abilities[index].visible;\r\n	end;\r\n	\r\n	return false;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetAbilitiesNumber()\r\n	return #(self.abilities);\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetAbilityIndex(abilityName)\r\n	for i,abilityEntry in pairs(self.abilities) do\r\n		if abilityEntry.name==abilityName or abilityEntry.paramsName == abilityName then\r\n			return i;\r\n		end;\r\n	end;\r\n	\r\n	return 0;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetAbilityName(index)\r\n	if index<=#(self.abilities) then\r\n		return self.abilities[index].name;\r\n	end;\r\n	\r\n	return nil;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:BlockAbilityGroup(abilityGroup, reason, flag)\r\n	local abilityGroupDisableCounter = self.abilityGroupDisableCounters[abilityGroup]; assert(abilityGroupDisableCounter);\r\n	abilityGroupDisableCounter:LockRef(reason, flag);	\r\n			\r\n	-- update blockades on abilities of this group\r\n	-- TODO: restrict this only to this group abilities\r\n	for i, ability in ipairs(self.abilities) do\r\n		self.remote:SetAbilityBlockade(i, ability.disableCounter:GetBlockade())\r\n	end\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:CollectPickup(pickup)\r\n	local abilityIndex = self:GetAbilityIndex(pickup:GetAbilityName());\r\n    local abilityCharges = (not ieq(abilityIndex,0)) and pickup:GetAbilityCharges()\r\n    if pickup:Collect(self) then\r\n        if abilityIndex and abilityCharges and igt(abilityCharges, 0) then\r\n	        self:AddAbilityChargesNumber(abilityIndex, abilityCharges, {tag=ITD2BasicHeroPlayerController.ABILITYCHARGES_PICKUPCOLLECTED, pickup=pickup});\r\n        end\r\n        return true\r\n    end\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TECHNOLOGY\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:CreateTechnologyManager()\r\n	assert(not self.technologyManager);\r\n	self.technologyManager = CreateObject(self.externalParams.TechnologyManager);\r\n    self.technologyManager:SetController(self)\r\n	self.technologyManager:Init(); assert(self.technologyManager.remote);\r\n	self.remote:SetTechnologyManager(self.technologyManager.remote);\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:DeleteTechnologyManager()\r\n	if self.technologyManager then\r\n		self.technologyManager:OnDelete();\r\n		self.technologyManager = nil\r\n	end\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetTechnologyManager()\r\n	return self.technologyManager;\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- CAMERA\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:BlockTacticalEnter(flag)\r\n    self.tacticalEnterBlocked = tobool(flag)\r\n	self.remote:BlockTacticalEnter(flag)\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:BlockTacticalExit(flag)\r\n    self.tacticalExitBlocked = tobool(flag)\r\n	self.remote:BlockTacticalExit(flag)\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:IsTacticalEnterBlocked()\r\n    return self.tacticalEnterBlocked;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:IsTacticalExitBlocked()\r\n    return self.tacticalExitBlocked;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- SAVE/LOAD\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:Save(data, writer)\r\n	ITD2BasicPlayerController.Save(self, data, writer)\r\n	data.tacticalEnterBlocked = self.tacticalEnterBlocked\r\n    data.tacticalExitBlocked = self.tacticalExitBlocked\r\n	data.unconsciousCounter = self.unconsciousCounter\r\n	data.resources = self.resources\r\n	\r\n	data.technology = {}\r\n	self.technologyManager:Save(data.technology)\r\n	\r\n	data.abilities={};\r\n	for i,v in ipairs(self.abilities) do\r\n		local newEntry={};\r\n		data.abilities[i] = newEntry;\r\n		-- TODO: unify variable names of config and member variables. so that these tables can be duplicated around\r\n		newEntry.Name = v.name;\r\n		newEntry.Charges = v.charges;\r\n		newEntry.cooldown = v.cooldown;\r\n		newEntry.Hidden = not v.visible;\r\n		newEntry.disableCounter = {}\r\n		v.disableCounter:Save(newEntry.disableCounter)\r\n	end;\r\n	\r\n	data.abilityGroupDisableCounters = {}\r\n	for abilityGroup, counter in pairs(self.abilityGroupDisableCounters) do\r\n		data.abilityGroupDisableCounters[abilityGroup] = {};\r\n		counter:Save(data.abilityGroupDisableCounters[abilityGroup]);\r\n	end\r\n	\r\n	data.abilityGlobalDisableCounter = {};\r\n	self.abilityGlobalDisableCounter:Save(data.abilityGlobalDisableCounter);\r\n	\r\n	-- postconditions\r\n	assert(data.abilities)\r\n	assert(data.resources)\r\n	assert(data.technology)\r\n	assert(data.abilityGlobalDisableCounter ~= nil)\r\n	assert(data.tacticalEnterBlocked ~= nil)\r\n	assert(data.tacticalExitBlocked ~= nil)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:DoLoad(data, reader)\r\n	ITD2BasicPlayerController.DoLoad(self, data, reader)\r\n	assert(data.abilities)\r\n	assert(data.resources)\r\n	assert(data.technology)\r\n	assert(data.abilityGlobalDisableCounter ~= nil)\r\n	assert(data.tacticalEnterBlocked ~= nil)\r\n	assert(data.tacticalExitBlocked ~= nil)\r\n	\r\n	self:CreateTechnologyManager();\r\n	self.technologyManager:Load(data.technology)\r\n	\r\n	self.unconsciousCounter = data.unconsciousCounter\r\n	self:SetResources(data.resources)\r\n	\r\n	self:BlockTacticalEnter(data.tacticalEnterBlocked)\r\n	self:BlockTacticalExit(data.tacticalExitBlocked)\r\n    --\r\n	-- abilities\r\n	self:SetAbilities(data.abilities);\r\n	\r\n	self.abilityGlobalDisableCounter:Load(data.abilityGlobalDisableCounter)\r\n	self.remote:BlockAbilities(self.abilityGlobalDisableCounter:IsLocked())\r\n	\r\n	for abilityGroup, counter in pairs(self.abilityGroupDisableCounters) do -- NOTE: we assume that SetAbilities will recreate all group disable counters\r\n		assert(data.abilityGroupDisableCounters[abilityGroup]);\r\n		counter:Load(data.abilityGroupDisableCounters[abilityGroup]);\r\n	end\r\n	\r\n	for i,v in ipairs(self.abilities) do\r\n		v.cooldown = data.abilities[i].cooldown;\r\n		v.disableCounter:Load(data.abilities[i].disableCounter);\r\n	end;\r\n	\r\n	-- we need to manually push blockades to remote\r\n	-- TODO: automatize this somehow\r\n	for i, ability in ipairs(self.abilities) do\r\n		self.remote:SetAbilityBlockade(i, ability.disableCounter:GetBlockade())\r\n	end\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- UPDATE\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:Update(gameplayPaused)\r\n	ITD2BasicPlayerController.Update(self, gameplayPaused);\r\n	self:UpdateAbilities();\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- EVENTS\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:OnOwnedEntityUnconscious(entity)\r\n	if entity == self:GetControlledEntity() then\r\n		self.unconsciousCounter = self.unconsciousCounter + 1\r\n		gGame.HQ:OnHeroObjectUnconscious(entity);\r\n	end\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- ACCESS\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: move to resources component\r\nfunction ITD2BasicHeroPlayerController:GetResources()\r\n	return self.resources*10000;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: move to resources component\r\nfunction ITD2BasicHeroPlayerController:SetResources(resources)\r\n	-- don't allow it to wrap!\r\n	-- NOTE: no assertion here to pass overnight tests\r\n	if resources > self.externalParams.MaxResources then\r\n		resources = self.externalParams.MaxResources\r\n	end\r\n\r\n	self.resources = resources\r\n	self.remote:SetResources(self.resources);\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: move to resources component\r\n-- NOTE: this methods takes resourceMultiplier in account whereas AddResources does not (which is desireable for selling etc.)\r\nfunction ITD2BasicHeroPlayerController:GainResources(resources, reason)\r\n    print(string.format(\"** PlayerController:GainResources(%d * %.3f)\", resources, self.GainedResourcesMultiplier))\r\n	self:AddResources(resources * self.GainedResourcesMultiplier, reason)\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: move to resources component\r\n-- convenience\r\nfunction ITD2BasicHeroPlayerController:AddResources(resources, reason)\r\n	self:SendEvent(ITD2BasicHeroPlayerController.Event_ResourcesChanged, {resources = resources, reason = reason});\r\n	gGame.HQ.SeqSystem:OnEmitCustomEvent(gGame.HQ, \"AddResources\", {resources = resources, reason = reason}); -- HACK: 1) HQ REFERENCE 2) REDUNDANT WITH SEND EVENT\r\n	\r\n	self:SetResources(self:GetResources() + resources)\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- TODO: move to resources component\r\n-- convenience\r\nfunction ITD2BasicHeroPlayerController:SubResources(resources, reason)\r\n	if self:GetResources() < resources then\r\n		return false;\r\n	end\r\n	\r\n	self:SendEvent(ITD2BasicHeroPlayerController.Event_ResourcesChanged, {resources = resources, reason = reason});\r\n	self:SetResources(self:GetResources() - resources)\r\n	return true;\r\nend;\r\n\r\n-------------------------------------------------------------------------------------------------\r\nfunction ITD2BasicHeroPlayerController:GetUnconsciousCounter()\r\n	return self.unconsciousCounter\r\nend\r\n\r\n-------------------------------------------------------------------------------------------------\r\n-- HACK: read it from somewhere else!\r\nfunction ITD2BasicHeroPlayerController:GetInitialResources()\r\n	return self.initialResources;\r\nend\r\n";
//	     //LogD("<%s> buff=%s", __FUNCTION__, rep);
//		return old_my_luaL_loadbuffer(L, rep, strlen(rep),name);
//	}

//LogD("luaL_loadbuffer L=0x%08X buff=0x%08X size=%d name=%s", L, buff, sz, name);
return old_my_luaL_loadbuffer(L, buff, sz, name);
}

int (*old_luaL_loadfile)(void *L, const char *filename) = NULL;
int my_luaL_loadfile(void *L, const char *filename) {
//start_now(L, 0);
LogD("luaL_loadfile L=0x%08X filename=%s", L, filename);
FILE * stream;
stream = fopen(filename, "rb");

//	if ((stream = fopen("/sdcard/wz/lua", "wb+")) == NULL) {
//				LogD("<%s> null:%s", __FUNCTION__, "null");
//			} else {
//				LogD("<%s> writer:%s", __FUNCTION__,
//						"/sdcard/wz/ starting writer flie");
//				int ret = fwrite(buff, sz, 1, stream);
//				if (ret) {
//					LogD("<%s> 文件写入完毕=%s", __FUNCTION__, result);
//
//				}
//				fclose(stream);
//			}
return old_luaL_loadfile(L, filename);
}

//int (*old_lua_loadx)(void *L, const char *filename) = NULL;
//int my_lua_loadx(void *L, const char *filename) {
//	//start_now(L, 0);
//	LogD("luaL_loadfile L=0x%08X filename=%s", L, filename);
//	FILE * stream;
//	stream =  fopen(filename, "rb");
//	return old_luaL_loadfile(L, filename);
//}

int (*old_luaL_loadstring)(void *L, const char *s) = NULL;
int my_luaL_loadstring(void *L, const char *s) {
//start_now(L, 0);
LogD("luaL_loadstring L=0x%08X s=%s", L, s);
return old_luaL_loadstring(L, s);
}
int (*old_GetScore)(int a1) = NULL;
int GetScore(int a1) {
int ret = old_GetScore(a1);
LogD("<%s> name=%s", __FUNCTION__, ret);
return ret;
}
const static HOOK_SYMBOL gHookSymbols[] = { { "luaL_loadbuffer",
	(void *) &my_luaL_loadbuffer, (void **) &old_my_luaL_loadbuffer },
//{"luaL_loadstring", (void *)&my_luaL_loadstring, (void **)&old_luaL_loadstring},
//{"luaL_loadfile", (void *)&my_luaL_loadfile, (void **)&old_luaL_loadfile},
//		{"lua_loadx", (void *)&my_lua_loadx, (void **)&old_lua_loadx},
	};
const static FIND_SYMBOL gFindSymbols[] = {

};

void hook_symbols(soinfo * handle) {

#ifdef NDK_DEBUG
LogD("<%s> %s %s handle = %x( funcs = %d)", __FUNCTION__, __DATE__,__TIME__,(int)handle, sizeof(gHookSymbols)/sizeof(gHookSymbols[0]));
#endif

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
