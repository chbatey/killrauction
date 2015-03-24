'use strict';

/* App Module */

var weatherApp = angular.module('killrAuction', [
    'ngWebsocket',
    'ngRoute',
    'ngTable',
    'auctionControllers',
    'ui.bootstrap.datetimepicker'
]);

weatherApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/auction', {
                templateUrl: 'auction-list.html',
                controller: 'AuctionListController'
            }).
            when('/auction-create', {
                templateUrl: 'auction-create.html',
                controller: 'AuctionCreationController'
            }).
            when('/auction-view/:auction', {
                templateUrl: 'auction-view.html',
                controller: 'AuctionViewController'
            }).
            when('/user', {
                templateUrl: 'user-create.html',
                controller: 'UserCreationController'
            }).
            otherwise({
                redirectTo: '/user'
            });
    }]);