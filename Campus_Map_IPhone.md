# Introduction #

This will be a brief overview of the campus map iPhone application

# Team Leader #
  * TBD within 2 weeks

# Meeting Time #
Tuesdays, 8-9PM (FGH 308)

Thursdays, 9-10PM (FGH 308)

You can come to either meeting

# Developers #
  * Guy Kopsombut
  * Demetri Miller
  * Ben Wibking

# User Stories #

Users can:

Map: Guy
  * <img src='http://ni2.in/tb/.png' /> see a map of campus
  * <img src='http://ni2.in/tb/.png' /> pan and zoom the map using single- and multi-touch gestures
  * <img src='http://ni2.in/tb/.png' /> tap a button that will move the map view to the center of campus and zoom to fit campus
  * **see the app decked out with the _official_ Vanderbilt color scheme**
  * rotate the device to reorient the map into portrait or landscape orientation
  * See POIs on the map as soon as they are loaded, instead of just when the layer is refreshed
  * See a loading screen when the app is first launched that indicates the first POIs are being loaded
Searching: Guy
  * Only see results calculated after two or more characters have been entered (avoiding a lag upon searching for just one letter)
  * Witness the map automatically pan and zoom the map to fit all search results
Layers: Ben, AJ
  * <img src='http://ni2.in/tb/.png' /> see the markers from the currently chosen layer
  * <img src='http://ni2.in/tb/.png' /> tap a button that will display the list of layers
  * <img src='http://ni2.in/tb/.png' /> select a layer from a list of a layer to display that one layer on the map
  * allow user to select one layer from a list of **multiple** layers
> > - see iPhone\_CampusMaps\_Implementation\_Details

Handles marker taps: Demetri
  * <img src='http://ni2.in/tb/.png' /> tap a location's marker to see its title and subtitle
  * <img src='http://ni2.in/tb/.png' /> tap a location marker's title to see the following details: title, details
  * <img src='http://ni2.in/tb/.png' /> see the annotation's details formatted as a list
  * <img src='http://ni2.in/tb/.png' /> see an image related to annotation selected when viewing the annotation's details
  * <img src='http://ni2.in/tb/.png' /> center image related to annotation selected when viewing annotation's details
  * <img src='http://ni2.in/tb/.png' /> see distance to selected annotation from current location

Locations: Joshua
  * <img src='http://ni2.in/tb/.png' /> see a search interface to search for POIs on the currently selected layer
  * **search for a location by name, hiding all locations that don't match**
  * cancel the search, restoring all previously shown locations
  * see the first search result in a view that slides up at the bottom of the window
  * swipe to see more search results
  * be panned to the annotation of the currently shown search result

Users would love to eventually at some distant point in the future:
  * see a compass rose that always points north
  * see a help layer when the app is first launched
  * add to the campus-wide list of locations
  * see context-sensitive details for supported locations
  * find the shortest path between any two points or buildings
  * find the shortest path from their current location to a location at or near Vanderbilt
  * see the locations of their courses on the map
  * see the locations of specific friends who are also using the app on the map
  * see the estimated occupancy of a building or rooms within a building
  * find office info provided by People Finder
  * see floorplans for buildings
  * search or browse by department, name, OASIS abbreviation, category


# Layers we'd like to see #
  * Buildings, plus the sub-layers
  * Residential
  * Dining
  * Class buildings
  * Parking
  * Trees
  * Wi-Fi strength
  * VandyVan routes
  * Taste of Nashville restaurants