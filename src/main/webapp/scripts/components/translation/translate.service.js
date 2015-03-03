'use strict';

angular.module('polcoApp')
    .factory('Translation',  ['$http', function($http) {

        var urlBase = '/api/translation';
        var dataFactory = {};

        dataFactory.getTranslation = function (textToTranslate) {
            return $http.post(urlBase, textToTranslate);
        };

        dataFactory.uploadFileToUrl = function(file){
                var fd = new FormData();
                fd.append('file', file);
                return $http.post(urlBase+"/import", fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                });
            };

        return dataFactory;
    }]);
