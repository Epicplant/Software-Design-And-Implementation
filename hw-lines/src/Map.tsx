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

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";
import mapLine from "./MapLine";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {

    //The parsed data of the "edges" text box
    lineList: string[][];

}

class Map extends Component<MapProps, {}> {

     //Turns parsed text lines into actual MapLine objects. Returns an array of these parsed MapLine objects
     handleCreateLines = (oldLines : string[][] = this.props.lineList) : JSX.Element[] => {

        let newLines: JSX.Element[] = [];

        //Creates a new  MapLine for every parsed line in oldLines
        for(let i : number = 0; i < this.props.lineList.length; i++) {
            newLines.push(<MapLine key={i} color={oldLines[i][4]} x1={parseFloat(oldLines[i][0])}
                                   y1={parseFloat(oldLines[i][1])} x2={parseFloat(oldLines[i][2])}
                                   y2={parseFloat(oldLines[i][3])}/>);
        }

        return newLines;
    }

  render() {
    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />


          {
              this.handleCreateLines(this.props.lineList)
          }

        </MapContainer>
      </div>
    );
  }
}

export default Map;