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
import {coloredEdge} from "./Types";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

// NOTE: This component is a suggestion for you to use, if you would like to. If
// you don't want to use this component, you're free to delete it or replace it
// with your hw-lines Map

interface MapProps {

    // Represents an array of paths that ultimately lead from a start building to an end building.
    // This path is the shortest possible way to get from the start building to the end building
    currentPath: coloredEdge[];
}




class Map extends Component<MapProps, {}> {

    // Constructs a series of MapLine components from currentPath which will be put
    // be placed within the Map.
    handleCreateLines = (path : coloredEdge[]) : JSX.Element[] => {

        // Array to hold the MapLine objects
        let completePath: JSX.Element[] = [];

        // Constructs a mapLine object for every path stored in currentPath.
        for(let i : number = 0; i < path.length; i++) {
            completePath.push(<MapLine key={i} color={"red"} x1={path[i].start.x}
                                   y1={path[i].start.y} x2={path[i].end.x}
                                   y2={path[i].end.y}/>);
        }

        return completePath;
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

              //Loads Created MapLines from handleCreateLines into the Map
              this.handleCreateLines(this.props.currentPath)
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
