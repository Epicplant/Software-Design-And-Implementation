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

interface EdgeListProps {

    //The callback to App.tsx that deals with changing the "edges" textbox text which is stored in App.tsx
    changeText(text: string): void;

    //The callback to App.tsx that deals with parsing the value within the "edges" textbox and then sending it
    //back to App where it later helps put it on the map.
    drawLines(edges : string[][]): void;

    //The callback to App.tsx that deals with changing the map so that there are no lines on it
    clearLines(): void;

    //The current text within the "edges" textbox
    textValue: string;
}


/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps> {

    //A method that utilizes the changeText callback in order to connect this program to App.tsx and update
    //the text value stored there.
    handleChangeText = (event : any) => {
        this.props.changeText(event.target.value);
    };

    //Takes the text inputted within the apps textbox and determines if it meets the specifications of the program.
    //For example, the string must have 5 elements, must have its first 4 elements be number coordinates between 0 and 4000,
    //and must have a color as its last element. Once it determines if the input meets the program's specifications
    //it then performs a callback with the parsed text (an array of arrays containing the data of each inputted line)
    //so that APP.tsx can regain control.
    handleDrawLines = () => {

        let temp : string = this.props.textValue;
        let lines : string[] = temp.split("\n");
        let returner : (string[])[] = [];

        //pushes valid edge lines onto the return array and returns early with nothing if one of the edges is invalid
        for(let i = 0; i < lines.length; i++) {
            let value : string[] = lines[i].split(" ");

            //Ends early if there is an incorrect number of edge parameters
            if(value.length != 5) {
                alert("Incorrect inputted number of edge parameters (must be 5)");
                return;
            }

            //The values of the coordinates from a parsed line
            let x1 : number = parseFloat(value[0]);
            let y1 : number = parseFloat(value[1]);
            let x2 : number = parseFloat(value[2]);
            let y2 : number = parseFloat(value[3]);


            //Returns early if any of the coordinate values are not a Number
            if(Number.isNaN(x1) || Number.isNaN(x2) || Number.isNaN(y1) || Number.isNaN(y2)) {
                alert("One or more of the point coordinates (one of the first 4 parameters) are not numbers");
                return;

            //Returns early if any of the coordinates are not between 0 and 4000
            } else if(x1 < 0 || x1 > 4000 || y1 < 0 || y1 > 4000 ||
                x2 < 0 || x2 > 4000 || y2 < 0 || y2 > 4000) {
                alert("Every line coordinate must be between 0 and 4000");
                return;
            }

            returner.push(value);
        }

        this.props.drawLines(returner);
    };

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={this.handleChangeText}
                    value={this.props.textValue}
                /> <br/>
                <button onClick={() => {this.handleDrawLines()}}>Draw</button>
                <button onClick={() => {this.props.clearLines()}}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;