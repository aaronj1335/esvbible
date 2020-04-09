package aaronstacy.esvbible.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.graphics.vector.VectorAsset
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Favorite
import androidx.ui.material.icons.filled.Search
import androidx.ui.material.icons.filled.Settings

enum class NavItem(val icon: VectorAsset) {
  Read(Icons.Filled.Favorite),
  Lookup(Icons.Filled.Search),
  Settings(Icons.Filled.Settings)
}

@Composable
fun Nav(modifier: Modifier, currentItem: NavItem, onItemSelected: (NavItem) -> Unit) {
  BottomNavigation(modifier) {
    for (item in NavItem.values()) {
      BottomNavigationItem(
        { Icon(item.icon) },
        selected = currentItem == item,
        onSelected = { onItemSelected(item) }
      )
    }
  }
}