/*
 * Copyright (C) 2022 Kevin Zatloukal and James Wilcox.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, { Component } from "react";
import EdgeList from "./EdgeList";
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import MapLine from "./MapLine";
import mapLine from "./MapLine";

interface AppState {

    //The parsed version of textValue. Is empty until drawLines is called
    lines: string[][];

    //The string currently within the edges text box
    textValue: string;

}


class App extends Component<{}, AppState> { // <- {} means no props.

    constructor(props: any) {
        super(props);


        this.state = {
          // TODO: store edges in this state
            lines : [],
            textValue : "",
        };
  }

  render() {
    return (
      <div>
        <h1 id="app-title">Line Mapper!</h1>
        <div>
          {/* TODO: define props in the Map component and pass them in here */}
          <Map lineList={this.state.lines}/>
        </div>
        <EdgeList
            
            textValue={this.state.textValue}
            changeText={(value) => {

                let newState = {
                    lines: this.state.lines,
                    textValue: value,
                };
                this.setState(newState);

            }}

            drawLines={(value) => {
                this.setState({lines: value, textValue: this.state.textValue,})
            }}

            clearLines={() => {
                this.setState({lines: [], textValue: this.state.textValue,});
            }}
        />
      </div>
    );
  }
}

export default App;
