'use strict';

angular.module('polcoApp')
    .factory('Pourcentage',  ['$http', function($http) {

        var urlBase = '/api/pourcentage';
        var dataFactory = {};

        dataFactory.getPourcentage = function (text) {
            return $http.post(urlBase, text);
        };

        return dataFactory;
    }]);
