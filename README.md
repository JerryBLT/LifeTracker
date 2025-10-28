# LifeTracker
CS501 (Q1) LifeTracker that logs and displays Android lifecycle events in real time. It visually indicate the current lifecycle state and log transitions in a scrolling list using LazyColumn.

## Features
- Logs Android lifecycle events (onCreate, onStart, onResume, onPause, onStop) in real time
- Displays lifecycle event logs in a scrolling list using LazyColumn
- Color-coded events for easy visual identification of lifecycle states
- Shows timestamps for each lifecycle event
- Snackbar notifications on lifecycle transitions, configurable via UI toggle
- Uses ViewModel to persist event logs across configuration changes
- Material 3 themed UI components with Compose

## Getting Started
### Prerequisites
- Android Studio (latest version recommended)

### How to Run
1. Clone the repository:
2. Open the project in Android Studio.

## Project Structure
- `MainActivity.kt`: Contains main Compose UI and state code.
- `ui.theme`: Contains app theme and styling.

## Usage
- Launch the app to see lifecycle events logged as you navigate or recreate the activity.
- Events appear with color-coded backgrounds and timestamps in a scrollable list.
- Snackbar pops up for each lifecycle transition, unless disabled via the top app bar action icon.
- Toggle the snackbar notifications on/off using the icon in the top right corner.
- Logs persist during configuration changes such as rotations because they’re stored in ViewModel.

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Author
Jerry Teixeria  
BU Email: jerrybt@bu.edu

