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

import React, {Component} from 'react';

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import Map from "./Map";
import BuildingDropdown from "./BuildingDropdown";
import {coloredEdge} from "./Types";
interface AppState {

    // An array containing the paths one must take in order to go from startBuilding and endBuilding
    // in the shortest amount of time possible.
    currentPath: coloredEdge[];

    // An array of <option> tags representing the names of available UW buildings
    buildings: JSX.Element[];

    // The currently selected building from the "start building" dropdown menu
    startBuilding: string;

    // The currently selected building from the "end building" dropdown menu
    endBuilding: string;

}

class App extends Component<{}, AppState> {


    // Constructs the initial state of the dropdown menus
    constructor(props: any) {
        super(props);

        this.state = {
            currentPath: [],
            buildings: [],
            startBuilding: "PAR",
            endBuilding: "PAR",
        };
    }


    render() {

        return (
            <div>
                <header id="head">Welcome to UW Campus Map!</header>
                <div>
                    <Map currentPath={this.state.currentPath}/>
                </div>
                <BuildingDropdown
                drawLines={ (value: coloredEdge[]) =>
                    // Updates the current state's currentPath field
                    this.setState({
                    currentPath: value})}
                clearLines={() =>
                    // Removes all data from the currentPath field
                    this.setState({  currentPath: []})}
                />

            </div>
        );
    }

}

export default App;
