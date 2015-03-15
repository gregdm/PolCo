'use strict';

angular.module('polcoApp')
    .factory('WordValidation', function ($resource) {
        return $resource('api/wordValidations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    //TODO GREG merge both factory
    .factory('WordValidationOK', function ($http) {
        return {
            validate: function (id) {
                return $http.get('api/wordValidations/validate/'+id).then(function (response) {
                    return response.data;
                });
            }
        }
    });
