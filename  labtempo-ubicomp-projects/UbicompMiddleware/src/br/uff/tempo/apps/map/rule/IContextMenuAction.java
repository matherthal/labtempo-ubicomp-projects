package br.uff.tempo.apps.map.rule;

/**
 * Action fired when an item is selected in ContextMenu
 * @see ContextMenu
 * @author dbarreto
 *
 */
public interface IContextMenuAction {

	/**
	 * Fired when an item is selected in ContextMenu
	 * 
	 * @param menu Reference to the menu that fired the event
	 * @param itemSelected Reference to the item that was selected in the ContextMenu
	 */
	void onContextMenuAction(ContextMenu menu, ContextMenuItem itemSelected);
}
