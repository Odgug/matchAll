package com.simibubi.create.content.logistics.filter;

import java.util.Arrays;
import java.util.List;

import com.simibubi.create.content.logistics.filter.FilterScreenPacket.Option;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

public class FilterScreen extends AbstractFilterScreen<FilterMenu> {

	private static final String PREFIX = "gui.filter.";

	private Component allowN = Lang.translateDirect(PREFIX + "allow_list");
	private Component allowDESC = Lang.translateDirect(PREFIX + "allow_list.description");
	private Component denyN = Lang.translateDirect(PREFIX + "deny_list");
	private Component denyDESC = Lang.translateDirect(PREFIX + "deny_list.description");

	private Component respectDataN = Lang.translateDirect(PREFIX + "respect_data");
	private Component respectDataDESC = Lang.translateDirect(PREFIX + "respect_data.description");
	private Component ignoreDataN = Lang.translateDirect(PREFIX + "ignore_data");
	private Component ignoreDataDESC = Lang.translateDirect(PREFIX + "ignore_data.description");

	private Component allowAnyN = Lang.translateDirect("gui.attribute_filter.allow_list_disjunctive");
	private Component allowAnyDESC = Lang.translateDirect("gui.attribute_filter.allow_list_disjunctive.description");
	private Component allowAllN = Lang.translateDirect("gui.attribute_filter.allow_list_conjunctive");
	private Component allowAllDESC = Lang.translateDirect("gui.attribute_filter.allow_list_conjunctive.description");


	private IconButton whitelist, blacklist;
	private IconButton respectNBT, ignoreNBT;

	private Indicator whitelistIndicator, blacklistIndicator;
	private Indicator respectNBTIndicator, ignoreNBTIndicator;

	private IconButton matchAny, matchAll;
	private Indicator matchAnyIndicator, matchAllIndicator;

	public FilterScreen(FilterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title, AllGuiTextures.FILTER);
	}

	@Override
	protected void init() {
		setWindowOffset(-11, 5);
		super.init();

		int x = leftPos;
		int y = topPos;

		blacklist = new IconButton(x + 18, y + 75, AllIcons.I_BLACKLIST);
		blacklist.withCallback(() -> {
			menu.blacklist = true;
			sendOptionUpdate(Option.BLACKLIST);
		});
		blacklist.setToolTip(denyN);
		whitelist = new IconButton(x + 36, y + 75, AllIcons.I_WHITELIST);
		whitelist.withCallback(() -> {
			menu.blacklist = false;
			sendOptionUpdate(Option.WHITELIST);
		});
		whitelist.setToolTip(allowN);
		blacklistIndicator = new Indicator(x + 18, y + 69, Components.immutableEmpty());
		whitelistIndicator = new Indicator(x + 36, y + 69, Components.immutableEmpty());
		addRenderableWidgets(blacklist, whitelist, blacklistIndicator, whitelistIndicator);

		respectNBT = new IconButton(x + 60, y + 75, AllIcons.I_RESPECT_NBT);
		respectNBT.withCallback(() -> {
			menu.respectNBT = true;
			sendOptionUpdate(Option.RESPECT_DATA);
		});
		respectNBT.setToolTip(respectDataN);
		ignoreNBT = new IconButton(x + 78, y + 75, AllIcons.I_IGNORE_NBT);
		ignoreNBT.withCallback(() -> {
			menu.respectNBT = false;
			sendOptionUpdate(Option.IGNORE_DATA);
		});
		ignoreNBT.setToolTip(ignoreDataN);
		respectNBTIndicator = new Indicator(x + 60, y + 69, Components.immutableEmpty());
		ignoreNBTIndicator = new Indicator(x + 78, y + 69, Components.immutableEmpty());
		addRenderableWidgets(respectNBT, ignoreNBT, respectNBTIndicator, ignoreNBTIndicator);


		//respect nbt - match all
		//ignore nbt - match any

		matchAll = new IconButton(x + 102, y + 75, AllIcons.I_WHITELIST_AND);
		matchAll.withCallback(() -> {
			menu.matchAll = true;
			sendOptionUpdate(Option.ADD_TAG);
		});
		matchAll.setToolTip(allowAllN);

		matchAny= new IconButton(x + 120, y + 75, AllIcons.I_WHITELIST_OR);
		matchAny.withCallback(() -> {
			menu.matchAll = false;
			sendOptionUpdate(Option.ADD_INVERTED_TAG);
		});
		matchAny.setToolTip(allowAnyN);
		matchAllIndicator= new Indicator(x + 102, y + 69, Components.immutableEmpty());
		matchAnyIndicator = new Indicator(x + 120, y + 69, Components.immutableEmpty());
		addRenderableWidgets(matchAll, matchAny, matchAllIndicator, matchAnyIndicator);
		handleIndicators();
	}

	@Override
	protected List<IconButton> getTooltipButtons() {
		return Arrays.asList(blacklist, whitelist, respectNBT, ignoreNBT, matchAll, matchAny);
	}

	@Override
	protected List<MutableComponent> getTooltipDescriptions() {
		return Arrays.asList(denyDESC.plainCopy(), allowDESC.plainCopy(), respectDataDESC.plainCopy(), ignoreDataDESC.plainCopy(), allowAllDESC.plainCopy(), allowAnyDESC.plainCopy());
	}

	@Override
	protected List<Indicator> getIndicators() {
		return Arrays.asList(blacklistIndicator, whitelistIndicator, respectNBTIndicator, ignoreNBTIndicator, matchAllIndicator, matchAnyIndicator);
	}

	@Override
	protected boolean isButtonEnabled(IconButton button) {
		if (button == blacklist)
			return !menu.blacklist;
		if (button == whitelist)
			return menu.blacklist;
		if (button == respectNBT)
			return !menu.respectNBT;
		if (button == ignoreNBT)
			return menu.respectNBT;
		if (button == matchAll)
			return !menu.matchAll;
		if (button == matchAny)
			return menu.matchAll;
		return true;
	}

	@Override
	protected boolean isIndicatorOn(Indicator indicator) {
		if (indicator == blacklistIndicator)
			return menu.blacklist;
		if (indicator == whitelistIndicator)
			return !menu.blacklist;
		if (indicator == respectNBTIndicator)
			return menu.respectNBT;
		if (indicator == ignoreNBTIndicator)
			return !menu.respectNBT;
		if (indicator == matchAllIndicator)
			return menu.matchAll;
		if (indicator == matchAnyIndicator)
			return !menu.matchAll;
		return false;
	}

}
