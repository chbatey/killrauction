'use strict';

/* App Module */

var weatherApp = angular.module('killrAuction', [
    'ngWebsocket',
    'ngRoute',
    'auctionControllers'
])

weatherApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/auction', {
                templateUrl: 'auction-list.html',
                controller: 'AuctionListController'
            }).
            otherwise({
                redirectTo: '/auction'
            });
    }]);