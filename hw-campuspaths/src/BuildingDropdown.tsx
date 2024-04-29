import React, {Component} from 'react';
import {coloredEdge} from "./Types";

interface DropdownProps {

    // Callback to App.tsx. Takes in an array of coloredEdge's and updates the currentPath field in
    // App.tsx's state to the array.
    drawLines(path : coloredEdge[]): void;

    // Callback to App.tsx. Clears the currentPath field in App.tsx's state
    clearLines(): void;

}

interface DropdownState {

    // An array of <option> tags representing the names of available UW buildings
    buildings: JSX.Element[];

    // The currently selected building from the "start building" dropdown menu
    startBuilding: string;

    // The currently selected building from the "end building" dropdown menu
    endBuilding: string;

}


/**
 * Two dropdown menus that allow a user to select the buildings we are finding a path between.
 * Also contains the buttons that the user will use to interact with the app.
 */
class BuildingDropdown extends Component<DropdownProps, DropdownState> {


    // Constructs the initial state of the dropdown menus
    constructor(props: any) {
        super(props);

        this.state = {
            buildings: [],
            startBuilding: "PAR",
            endBuilding: "PAR",
        };
    }


    // Queries the spark server at localhost:4567/campus-buildings to retrieve a record
    // where the keys are every UW building's abbreviated name and their associated values
    // are those buildings' full names.
    async loadBuildings() {
        try {
            // Queries spark server
            // The variable response waits for the fetch to return the value from the server before moving on.
            let response = await fetch("http://localhost:4567/campus-buildings");

            // Check that no error occurred while contacting spark server
            if (!response.ok) {
                alert("Error! Expected: 200, Was: " + response.status);
                return;
            }

            // Parses the returned json from the spark server into a record that can be iterated over
            let parsed : Record<string, string> = await response.json();
            let returner : JSX.Element[] = [];
            let counter : number = 0;

            // Loops through every single value in the returned record and creates option tags from them which are
            // then storred in the returner array
            for(let shortName in parsed) {
                returner.push(<option key={counter} value={shortName}>
                    { parsed[shortName] + " (" + shortName + ")"}</option>);
                counter++;
            }

            this.setState({  buildings: returner,
                startBuilding: this.state.startBuilding, endBuilding: this.state.endBuilding});

        } catch (e) { //catches any errors that occur
            alert("There was an error contacting the server.");
        }
    }

    // Queries the spark server at localhost:4567/campus-path to retrieve a path object
    // describing the coordinate points one should move to sequentially in order to make
    // the journey from a start building to an end building in the shortest amount of time possible.
    async findShortestPath() {
        try {
            // Queries spark server with the values of startBuilding and endBuilding being the query parameters.
            // The variable response waits for the fetch to return the value from the server before moving on.
            let response = await fetch("http://localhost:4567/campus-path?start=" + this.state.startBuilding +
                "&end=" + this.state.endBuilding);

            // Check that no error occurred while contacting spark server
            if (!response.ok) {
                alert("Error! Expected: 200, Was: " + response.status);
                return;
            }

            // Parses the queried response into an array of paths one can take to get from
            // the start building to the end building in the shortest amount of time. Updates
            // the currentPath field with this array using the drawLines callback.
            let parsed  = await response.json();
            let path : coloredEdge[] = parsed.path;
            this.props.drawLines(path);

        } catch (e) { //catches any errors that occur
            alert("There was an error contacting the server.");
        }
    }


    // Clears any lines drawn on the map while also resetting all state values to their original form
    // (The way it was when the page was loaded so both the states in App and in BuildingDropdown)
    handleClearLines = () => {

        let newState = {
            buildings: this.state.buildings,
            startBuilding: "PAR", endBuilding: "PAR"
        }

        this.setState(newState);

        this.props.clearLines();

    }

    // Used to call findShortestPath from an arrow function so it works properly with onClick and async
    handleShortestPath = async () => {
        await this.findShortestPath();
    }

    // Calls loadBuildings as soon as the component loads so that the dropdown menus are always populated
    async componentDidMount() {
        await this.loadBuildings();
    }

    render() {

        return (

            <div id="building-dropdown" onLoad={this.loadBuildings}>
                 <div id="dropdowns" >
                    <div>
                        <h1 className="dropdown-heads">Start Building</h1>
                        <select value={this.state.startBuilding} className="dropdown-params"
                                onChange={(event : React.ChangeEvent<HTMLSelectElement>) => {
                                    this.setState({
                                        buildings: this.state.buildings,
                                        startBuilding: event.target.value,
                                        endBuilding: this.state.endBuilding
                                    })}}>
                            {this.state.buildings}
                        </select>
                    </div>
                    <div>
                        <h1 className="dropdown-heads">End Building</h1>
                        <select value={this.state.endBuilding} className="dropdown-params"
                                onChange={(event : React.ChangeEvent<HTMLSelectElement>) => {
                                    this.setState({
                                        buildings: this.state.buildings,
                                        startBuilding: this.state.startBuilding,
                                        endBuilding: event.target.value
                                    })}}>
                            {this.state.buildings}
                        </select>
                    </div>
                </div>
                <div>
                    <button className = "button" onClick={this.handleShortestPath}>Draw</button>
                    <button className = "button" onClick={this.handleClearLines}>Clear</button>
                </div>
            </div>
        );
    }


}

export default BuildingDropdown;