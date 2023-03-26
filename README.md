# Android Project 5 - *BitFit*

Submitted by: **Sharon Chen**

**BitFit** is a health metrics app that allows users to track sleep and exercise hours

Time spent: **10** hours spent in total

## Required Features

The following **required** functionality is completed:

- [x] **At least one health metric is tracked (based on user input)**
  - Chosen metric(s): `sleep and exercise hours`
- [x] **There is a "create entry" UI that prompts users to make their daily entry**
- [x] **New entries are saved in a database and then updated in the RecyclerView**
- [x] **On application restart, previously entered entries are preserved (i.e., are *persistent*)**
 
The following **optional** features are implemented:

- [x] **Create a UI for tracking averages and trends in metrics**
- [x] **Improve and customize the user interface through styling and coloring**
- [ ] **Implement orientation responsivity**
- [ ] **Add a daily photo feature**

The following **additional** features are implemented:

- [x] **Let user select the date to input data, and make sure no duplicate entries for the same date**
- [x] **Long press a record to delete**

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://user-images.githubusercontent.com/69126372/227751530-a80d27b6-a4f0-44ce-b735-e3177fe844ef.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<!-- Replace this with whatever GIF tool you used! -->
GIF created with AZ Screen Recorder (on Android phone) and ScreenToGif to screen recording and convert to gif 
<!-- Recommended tools:
[Kap](https://getkap.co/) for macOS
[ScreenToGif](https://www.screentogif.com/) for Windows
[peek](https://github.com/phw/peek) for Linux. -->

## Notes

- Database using Room is challenging, since set Date as primary key, did some try and error on preventing duplicated entries for same date
- Learned that updating UI related in  lifecycleScope.launch() is impossible, need to use Handler(Looper.getMainLooper()).post{} to do so.
- the getAll() database method in lifecycleScope.launch() in MainActivity runs every time there's changes in database. So don't need to call
adapter.notifyDataSetChanged() when inserting and deleting an item into/from database
- Self-studied on implementing  Seekbar and Seekbar.setOnSeekBarChangeListener(), and DatePickerDialog. Noticed that button can be used as spinnerStyle for DatePickerDialog and a text box.

## License

    Copyright [2023] [Sharon Chen]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
