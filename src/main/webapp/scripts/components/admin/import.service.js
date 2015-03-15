'use strict';

angular.module('polcoApp')
    .factory('ImportService',  ['$http', function($http) {

        var urlBase = '/api/import';
        var dataFactory = {};

        dataFactory.uploadFileToUrl = function(file){
            var fd = new FormData();
            fd.append('file', file);
            return $http.post(urlBase+"/CSV", fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            });
        };

        dataFactory.uploadFileXML = function(file){
            var fd = new FormData();
            fd.append('file', file);
            return $http.post(urlBase+"/XML", fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            });
        };

        return dataFactory;
    }]);

